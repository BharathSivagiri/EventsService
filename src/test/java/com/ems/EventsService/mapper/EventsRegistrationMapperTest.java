package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.RegistrationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventsRegistrationMapperTest {

    private EventsRegistrationMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new EventsRegistrationMapper();
    }

    @Test
    void toEntity_ShouldMapAllFieldsCorrectly() {
        // Given
        String transactionId = "TRANS123";
        String eventId = "1";
        String userId = "2";
        String createdBy = "TestUser";

        // When
        EventsRegistration result = mapper.toEntity(transactionId, eventId, userId, createdBy);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getEventId());
        assertEquals(2, result.getUserId());
        assertEquals("TRANS123", result.getTransactionId());
        assertEquals(RegistrationStatus.REGISTERED, result.getRegistrationStatus());
        assertEquals("TestUser", result.getCreatedBy());
        assertEquals("TestUser", result.getLastUpdatedBy());
        assertEquals(DBRecordStatus.ACTIVE, result.getRecordStatus());
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getLastUpdatedDate());
    }

    @Test
    void toEntity_ShouldHandleNumericInputsCorrectly() {
        // Given
        String transactionId = "TRANS456";
        String eventId = "100";
        String userId = "200";
        String createdBy = "Admin";

        // When
        EventsRegistration result = mapper.toEntity(transactionId, eventId, userId, createdBy);

        // Then
        assertEquals(100, result.getEventId());
        assertEquals(200, result.getUserId());
    }

    @Test
    void toEntity_ShouldThrowNumberFormatException_WhenInvalidNumericInput() {
        // Given
        String transactionId = "TRANS789";
        String eventId = "invalid";
        String userId = "300";
        String createdBy = "System";

        // Then
        assertThrows(NumberFormatException.class, () ->
            mapper.toEntity(transactionId, eventId, userId, createdBy));
    }
}
