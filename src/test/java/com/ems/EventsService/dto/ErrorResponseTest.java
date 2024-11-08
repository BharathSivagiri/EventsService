package com.ems.EventsService.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseConstructorAndGetters() {
        String errorCode = "ERR001";
        String errorMessage = "Test error message";

        ErrorResponse errorResponse = new ErrorResponse(errorCode, errorMessage);

        assertEquals(errorCode, errorResponse.getErrorCode());
        assertEquals(errorMessage, errorResponse.getErrorMessage());
    }

    @Test
    void testErrorResponseSetters() {
        ErrorResponse errorResponse = new ErrorResponse("ERR001", "Initial message");

        String newErrorCode = "ERR002";
        String newErrorMessage = "Updated error message";

        errorResponse.setErrorCode(newErrorCode);
        errorResponse.setErrorMessage(newErrorMessage);

        assertEquals(newErrorCode, errorResponse.getErrorCode());
        assertEquals(newErrorMessage, errorResponse.getErrorMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ErrorResponse response1 = new ErrorResponse("ERR001", "Test message");
        ErrorResponse response2 = new ErrorResponse("ERR001", "Test message");
        ErrorResponse response3 = new ErrorResponse("ERR002", "Different message");

        assertTrue(response1.equals(response2));
        assertEquals(response1.hashCode(), response2.hashCode());
        assertFalse(response1.equals(response3));
    }

    @Test
    void testToString() {
        ErrorResponse errorResponse = new ErrorResponse("ERR001", "Test message");
        String toString = errorResponse.toString();

        assertTrue(toString.contains("ERR001"));
        assertTrue(toString.contains("Test message"));
    }
}
