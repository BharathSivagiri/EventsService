//package com.ems.EventsService.controller;
//
//import com.ems.EventsService.dto.LoginRequest;
//import com.ems.EventsService.dto.LoginResponse;
//import com.ems.EventsService.services.AuthService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.when;
//
//class AuthControllerTest {
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    private LoginRequest loginRequest;
//    private static final String TEST_TOKEN = "test-jwt-token";
//    private static final String TEST_USERNAME = "JohnDoe123";
//    private static final String TEST_PASSWORD = "password123";
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        loginRequest = new LoginRequest();
//        loginRequest.setCustomName(TEST_USERNAME);
//        loginRequest.setPassword(TEST_PASSWORD);
//    }
//
//    @Test
//    void login_Success() {
//        // Arrange
//        when(authService.authenticateUser(TEST_USERNAME, TEST_PASSWORD)).thenReturn(TEST_TOKEN);
//
//        // Act
//        ResponseEntity<LoginResponse> response = authController.login(loginRequest);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals(TEST_TOKEN, response.getBody().getToken());
//    }
//}