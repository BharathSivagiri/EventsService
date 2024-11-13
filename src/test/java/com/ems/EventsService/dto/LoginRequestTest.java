//package com.ems.EventsService.dto;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class LoginRequestTest {
//
//    @Test
//    void testLoginRequestGettersAndSetters() {
//        // Create instance
//        LoginRequest loginRequest = new LoginRequest();
//
//        // Test setters
//        loginRequest.setCustomName("JohnDoe123");
//        loginRequest.setPassword("password123");
//
//        // Test getters
//        assertEquals("JohnDoe123", loginRequest.getCustomName());
//        assertEquals("password123", loginRequest.getPassword());
//    }
//
//    @Test
//    void testLoginRequestEqualsAndHashCode() {
//        LoginRequest request1 = new LoginRequest();
//        request1.setCustomName("JohnDoe123");
//        request1.setPassword("password123");
//
//        LoginRequest request2 = new LoginRequest();
//        request2.setCustomName("JohnDoe123");
//        request2.setPassword("password123");
//
//        // Test equals
//        assertEquals(request1, request2);
//
//        // Test hashCode
//        assertEquals(request1.hashCode(), request2.hashCode());
//    }
//
//    @Test
//    void testLoginRequestToString() {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setCustomName("JohnDoe123");
//        loginRequest.setPassword("password123");
//
//        String toString = loginRequest.toString();
//
//        assertTrue(toString.contains("JohnDoe123"));
//        assertTrue(toString.contains("password123"));
//    }
//}
