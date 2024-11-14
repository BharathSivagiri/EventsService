package com.ems.EventsService.mapper;

import com.ems.EventsService.model.PaymentRequestDTO;
import com.ems.EventsService.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class PaymentAccountMapper {
    public PaymentRequestDTO mapUserAccountToPaymentRequest(PaymentRequestDTO request, Users user) {
        request.setAccountNumber(user.getAccount());
        return request;
    }
}
