package com.ems.EventsService.exceptions.custom;

public class TokenExpiredException extends RuntimeException
{
    public TokenExpiredException(String message)
    {
        super(message);
    }
}
