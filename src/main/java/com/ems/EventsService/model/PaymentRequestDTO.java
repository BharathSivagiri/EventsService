package com.ems.EventsService.model;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String eventId;
    private String userId;
    private String amountPaid;
    private String paymentMode;
    private String accountNumber;
    private String transactionType;
    private String createdBy;
    private String paymentStatus;
}

