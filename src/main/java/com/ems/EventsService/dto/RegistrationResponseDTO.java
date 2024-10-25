package com.ems.EventsService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponseDTO {
    private Integer registrationId;
    private String status;
    private String message;
}

