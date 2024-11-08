package com.ems.EventsService.utility.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorMessagesTest {

    @Test
    void verifyEventRelatedErrorMessages() {
        assertEquals("Event name cannot be empty", ErrorMessages.EVENT_NAME_EMPTY);
        assertEquals("An event with this name already exists", ErrorMessages.EVENT_NAME_EXISTS);
        assertEquals("Event date cannot be null", ErrorMessages.EVENT_DATE_NULL);
        assertEquals("Event date cannot be in the past", ErrorMessages.EVENT_DATE_PAST);
        assertEquals("Event not found", ErrorMessages.EVENT_NOT_FOUND);
        assertEquals("Invalid date format. Please use yyyyMMdd format.", ErrorMessages.INVALID_DATE_FORMAT);
        assertEquals("Event is already at full capacity", ErrorMessages.EVENT_FULL_CAPACITY);
        assertEquals("Event does not exist. Try again", ErrorMessages.EVENT_RETRIEVAL_ERROR);
        assertEquals("Event created successfully", ErrorMessages.EVENT_CREATED);
        assertEquals("Event updated successfully", ErrorMessages.EVENT_UPDATED);
    }

    @Test
    void verifyUserRelatedErrorMessages() {
        assertEquals("User not found", ErrorMessages.USER_NOT_FOUND);
        assertEquals("Invalid password", ErrorMessages.INVALID_PASSWORD);
        assertEquals("User mismatch", ErrorMessages.USER_MISMATCH);
        assertEquals("User is already registered for this event", ErrorMessages.USER_ALREADY_REGISTERED);
    }

    @Test
    void verifyTokenRelatedErrorMessages() {
        assertEquals("Invalid token", ErrorMessages.INVALID_TOKEN);
        assertEquals("Token is expired", ErrorMessages.TOKEN_EXPIRED);
        assertEquals("Token is inactive", ErrorMessages.TOKEN_INACTIVE);
    }

    @Test
    void verifyRegistrationRelatedErrorMessages() {
        assertEquals("Registration not found", ErrorMessages.REGISTRATION_NOT_FOUND);
        assertEquals("Invalid registration status", ErrorMessages.INVALID_REG_STATUS);
        assertEquals("Cannot cancel registration for past events", ErrorMessages.PAST_EVENT_CANCELLATION);
        assertEquals("Registration is already cancelled", ErrorMessages.ALREADY_CANCELLED);
    }

    @Test
    void verifyEmailRelatedErrorMessages() {
        assertEquals("Failed to send email", ErrorMessages.EMAIL_NOT_SENT);
        assertEquals("Email template not found", ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND);
    }

    @Test
    void verifyStatusRelatedErrorMessages() {
        assertEquals("Invalid user status", ErrorMessages.INVALID_USER_STATUS);
        assertEquals("Invalid event status", ErrorMessages.INVALID_EVENT_STATUS);
        assertEquals("Record not found", ErrorMessages.RECORD_NOT_FOUND);
    }

    @Test
    void verifySearchRelatedErrorMessages() {
        assertEquals("No events found for the given criteria", ErrorMessages.NO_EVENTS_FOUND);
        assertEquals("Date1 must be before or equal to Date2", ErrorMessages.INVALID_DATE_RANGE);
    }

    @Test
    void verifyFormattedErrorMessages() {
        String eventId = "123";
        String expectedMessage = String.format("Event with ID '%s' has been marked as inactive.", eventId);
        assertEquals(expectedMessage, String.format(ErrorMessages.EVENT_DELETED, eventId));
    }
}
