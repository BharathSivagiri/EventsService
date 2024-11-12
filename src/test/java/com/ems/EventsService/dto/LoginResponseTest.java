//package com.ems.EventsService.dto;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class LoginResponseTest {
//
//    @Test
//    void testLoginResponse() {
//        // Given
//        String expectedToken = "test-jwt-token";
//
//        // When
//        LoginResponse loginResponse = new LoginResponse(expectedToken);
//
//        // Then
//        assertEquals(expectedToken, loginResponse.getToken());
//    }
//
//    @Test
//    void testLoginResponseSetterGetter() {
//        // Given
//        LoginResponse loginResponse = new LoginResponse("initial-token");
//        String newToken = "updated-token";
//
//        // When
//        loginResponse.setToken(newToken);
//
//        // Then
//        assertEquals(newToken, loginResponse.getToken());
//    }
//
//    @Test
//    void testEqualsAndHashCode() {
//        // Given
//        String token = "same-token";
//        LoginResponse response1 = new LoginResponse(token);
//        LoginResponse response2 = new LoginResponse(token);
//
//        // Then
//        assertEquals(response1, response2);
//        assertEquals(response1.hashCode(), response2.hashCode());
//    }
//}
