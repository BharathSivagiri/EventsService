package com.ems.EventsService.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String eventId;
    private String userId;
    private String amountPaid;
    private String paymentMode;
    private String bankId;
    private String transactionType;
    private String createdBy;
    private String paymentStatus;
}

