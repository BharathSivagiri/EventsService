package com.ems.EventsService.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponseDTO {
    private Integer registrationId;
    private String status;
    private String message;
}

