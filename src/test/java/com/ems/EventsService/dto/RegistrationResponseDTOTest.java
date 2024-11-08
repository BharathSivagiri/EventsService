package com.ems.EventsService.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationResponseDTOTest {

    @Test
    void testRegistrationResponseDTO() {
        // Given
        Integer registrationId = 1;
        String status = "SUCCESS";
        String message = "Registration completed successfully";

        // When
        RegistrationResponseDTO response = new RegistrationResponseDTO(registrationId, status, message);

        // Then
        assertEquals(registrationId, response.getRegistrationId());
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
    }

    @Test
    void testSettersAndGetters() {
        // Given
        RegistrationResponseDTO response = new RegistrationResponseDTO(null, null, null);

        // When
        response.setRegistrationId(2);
        response.setStatus("PENDING");
        response.setMessage("Registration in progress");

        // Then
        assertEquals(2, response.getRegistrationId());
        assertEquals("PENDING", response.getStatus());
        assertEquals("Registration in progress", response.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        // Given
        RegistrationResponseDTO response1 = new RegistrationResponseDTO(1, "SUCCESS", "Done");
        RegistrationResponseDTO response2 = new RegistrationResponseDTO(1, "SUCCESS", "Done");
        RegistrationResponseDTO response3 = new RegistrationResponseDTO(2, "FAILED", "Error");

        // Then
        assertEquals(response1, response2);
        assertNotEquals(response1, response3);
        assertEquals(response1.hashCode(), response2.hashCode());
    }
}
