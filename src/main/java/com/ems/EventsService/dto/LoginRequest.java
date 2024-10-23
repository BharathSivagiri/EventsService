package com.ems.EventsService.dto;

import lombok.Data;

@Data
public class LoginRequest
{
    private String customName;
    private String password;
}

