//package com.ems.EventsService.dto;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class PaymentRequestDTOTest {
//
//    @Test
//    void testPaymentRequestDTO() {
//        // Arrange
//        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
//
//        // Act
//        paymentRequest.setEventId("EVENT123");
//        paymentRequest.setUserId("USER456");
//        paymentRequest.setAmountPaid("100.00");
//        paymentRequest.setPaymentMode("CREDIT_CARD");
//        paymentRequest.setAccountNumber("ACC789");
//        paymentRequest.setTransactionType("PAYMENT");
//        paymentRequest.setCreatedBy("TestUser");
//        paymentRequest.setPaymentStatus("SUCCESS");
//
//        // Assert
//        assertEquals("EVENT123", paymentRequest.getEventId());
//        assertEquals("USER456", paymentRequest.getUserId());
//        assertEquals("100.00", paymentRequest.getAmountPaid());
//        assertEquals("CREDIT_CARD", paymentRequest.getPaymentMode());
//        assertEquals("ACC789", paymentRequest.getAccountNumber());
//        assertEquals("PAYMENT", paymentRequest.getTransactionType());
//        assertEquals("TestUser", paymentRequest.getCreatedBy());
//        assertEquals("SUCCESS", paymentRequest.getPaymentStatus());
//    }
//
//    @Test
//    void testPaymentRequestDTOEqualsAndHashCode() {
//        // Arrange
//        PaymentRequestDTO request1 = new PaymentRequestDTO();
//        PaymentRequestDTO request2 = new PaymentRequestDTO();
//
//        request1.setEventId("3");
//        request1.setUserId("6");
//        request2.setEventId("3");
//        request2.setUserId("6");
//
//        // Assert
//        assertEquals(request1, request2);
//        assertEquals(request1.hashCode(), request2.hashCode());
//    }
//}
