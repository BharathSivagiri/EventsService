package com.ems.EventsService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancellationResponseDTO {
    private Integer registrationId;
    private String status;
    private String message;
    private Double refundAmount;
    private String refundTransactionId;
}

