package com.ems.EventsService.mapper;

import com.ems.EventsService.model.PaymentRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestMapper {
    public PaymentRequestDTO toRefundRequest(PaymentRequestDTO originalRequest) {
        PaymentRequestDTO refundRequest = new PaymentRequestDTO();
        refundRequest.setEventId(originalRequest.getEventId());
        refundRequest.setUserId(originalRequest.getUserId());
        refundRequest.setAmountPaid(originalRequest.getAmountPaid());
        refundRequest.setPaymentMode(originalRequest.getPaymentMode());
        refundRequest.setAccountNumber(originalRequest.getAccountNumber());
        refundRequest.setTransactionType(originalRequest.getTransactionType());
        refundRequest.setPaymentStatus(originalRequest.getPaymentStatus());
        refundRequest.setCreatedBy(originalRequest.getCreatedBy());
        return refundRequest;
    }

}
