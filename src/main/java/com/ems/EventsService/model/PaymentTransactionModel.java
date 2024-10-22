package com.ems.EventsService.model;

import lombok.Data;

@Data
public class PaymentTransactionModel
{
    private String id;
    private String eventId;
    private String amountPaid;
    private String paymentMode;
    private String transactionType;
    private String paymentStatus;
    private String createdBy;
}
