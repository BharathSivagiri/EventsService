package com.ems.EventsService.services.implementations;

import com.ems.EventsService.model.PaymentRequestDTO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class PaymentClientServiceImpl {

    private final RestTemplate restTemplate;
    private final String paymentsServiceUrl;

    public PaymentClientServiceImpl(
            RestTemplate restTemplate,
            @Value("${payments.service.url}") String paymentsServiceUrl) {
        this.restTemplate = restTemplate;
        this.paymentsServiceUrl = paymentsServiceUrl;
    }

    public Integer processPayment(PaymentRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        Integer transactionId = restTemplate.postForObject(
                paymentsServiceUrl + "/payment/process",
                requestEntity,
                Integer.class
        );

        log.info("Payment processed successfully with ID: {}", transactionId);
        return transactionId;
    }

    public Integer processRefund(PaymentRequestDTO request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PaymentRequestDTO> requestEntity = new HttpEntity<>(request, headers);

        Integer transactionId = restTemplate.postForObject(
                paymentsServiceUrl + "/payment/refund",
                requestEntity,
                Integer.class
        );

        log.info("Refund processed successfully with ID: {}", transactionId);
        return transactionId;
    }


}

