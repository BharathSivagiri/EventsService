package com.ems.EventsService.controller;

import com.ems.EventsService.dto.LoginRequest;
import com.ems.EventsService.dto.LoginResponse;
import com.ems.EventsService.services.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_SuccessfulAuthentication() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustomName("testUser");
        loginRequest.setPassword("testPass");
        String expectedToken = "jwt-token";

        when(authService.authenticateUser(anyString(), anyString())).thenReturn(expectedToken);

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        // Assert
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertEquals(expectedToken, response.getBody().getToken());
    }

    @Test
    void login_FailedAuthentication() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setCustomName("testUser");
        loginRequest.setPassword("wrongPass");
        String errorMessage = "Invalid credentials";

        when(authService.authenticateUser(anyString(), anyString()))
            .thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        // Assert
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getToken());
    }
}
