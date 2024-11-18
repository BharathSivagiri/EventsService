package com.ems.EventsService.model;

import lombok.Data;

@Data
public class LoginRequest
{
    private String customName;
    private String password;
}

