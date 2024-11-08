package com.ems.EventsService.services.implementations;

import com.ems.EventsService.dto.PaymentRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private PaymentClientService paymentClientService;
    private static final String PAYMENT_SERVICE_URL = "http://payment-service";

    @BeforeEach
    void setUp() {
        paymentClientService = new PaymentClientService(restTemplate, PAYMENT_SERVICE_URL);
    }

    @Test
    void processPayment_Success() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setEventId("1");
        request.setUserId("4");
        Integer expectedTransactionId = 7;

        when(restTemplate.postForObject(
            eq(PAYMENT_SERVICE_URL + "/payment/process"),
            any(HttpEntity.class),
            eq(Integer.class)
        )).thenReturn(expectedTransactionId);

        // Act
        Integer result = paymentClientService.processPayment(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTransactionId, result);
    }

    @Test
    void processRefund_Success() {
        // Arrange
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setEventId("1");
        request.setUserId("4");
        Integer expectedTransactionId = 7;

        when(restTemplate.postForObject(
            eq(PAYMENT_SERVICE_URL + "/payment/refund"),
            any(HttpEntity.class),
            eq(Integer.class)
        )).thenReturn(expectedTransactionId);

        // Act
        Integer result = paymentClientService.processRefund(request);

        // Assert
        assertNotNull(result);
        assertEquals(expectedTransactionId, result);
    }
}
