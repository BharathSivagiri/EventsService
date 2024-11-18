package com.ems.EventsService.exceptions.custom;

public class BasicValidationException extends RuntimeException {
    public BasicValidationException(String message) {
        super(message);
    }
}
