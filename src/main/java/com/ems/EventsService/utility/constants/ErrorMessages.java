package com.ems.EventsService.utility.constants;

public class ErrorMessages {
    public static final String EVENT_NAME_EMPTY = "Event name cannot be empty";
    public static final String EVENT_NAME_EXISTS = "An event with this name already exists";
    public static final String EVENT_DATE_NULL = "Event date cannot be null";
    public static final String EVENT_DATE_PAST = "Event date cannot be in the past";
    public static final String EVENT_NOT_FOUND = "Event not found";
    public static final String INVALID_DATE_FORMAT = "Invalid date format. Please use yyyyMMdd format.";
    public static final String EVENT_FULL_CAPACITY = "Event is already at full capacity";
    public static final String EMAIL_NOT_SENT = "Failed to send email";
    public static final String EMAIL_TEMPLATE_NOT_FOUND = "Email template not found";
    public static final String INVALID_EVENT_ID = "Invalid event ID";
    public static final String EVENT_RETRIEVAL_ERROR = "Event does not exist. Try again";
    public static final String ACCESS_DENIED = "Access denied";
    public static final String EVENT_ALREADY_REGISTERED = "User is already registered for this event";
    public static final String EVENT_CREATED = "Event created successfully";
    public static final String EVENT_UPDATED = "Event updated successfully";
    public static final String EVENT_DELETED = "Event with ID '%s' has been marked as inactive.";

    public static final String USER_NOT_FOUND = "User not found";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String USER_MISMATCH = "User mismatch";
    public static final String TOKEN_EXPIRED = "Token is expired";
    public static final String TOKEN_INACTIVE = "Token is inactive";
    public static final String REGISTRATION_NOT_FOUND = "Registration not found";
    public static final String RECORD_NOT_FOUND = "Record not found";
    public static final String INVALID_REG_STATUS = "Invalid registration status";
    public static final String INVALID_USER_STATUS = "Invalid user status";
    public static final String INVALID_EVENT_STATUS = "Invalid event status";
    public static final String USER_ALREADY_REGISTERED = "User is already registered for this event";
    public static final String PAST_EVENT_CANCELLATION = "Cannot cancel registration for past events";
    public static final String ALREADY_CANCELLED = "Registration is already cancelled";
    public static final String NO_EVENTS_FOUND = "No events found for the given criteria";
    public static final String INVALID_DATE_RANGE = "Date1 must be before or equal to Date2";

    public static final String INVALID_EMAIL = "Invalid Email format";
}

