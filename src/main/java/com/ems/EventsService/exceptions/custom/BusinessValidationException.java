package com.ems.EventsService.exceptions.custom;

public class BusinessValidationException extends RuntimeException
{
    public BusinessValidationException(String message)
    {
        super(message);
    }
}
