//package com.ems.EventsService.exceptions.global;
//
//import com.ems.EventsService.exceptions.custom.BusinessValidationException;
//import com.ems.EventsService.exceptions.custom.DataNotFoundException;
//import com.ems.EventsService.exceptions.custom.PaymentProcessingException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.HttpStatus;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//class GlobalExceptionHandlerTest {
//
//    private GlobalExceptionHandler exceptionHandler;
//
//    @BeforeEach
//    void setUp() {
//        exceptionHandler = new GlobalExceptionHandler();
//    }
//
//    @Test
//    void handleBusinessValidationException_ReturnsCorrectResponse() {
//        String errorMessage = "Validation failed";
//        BusinessValidationException exception = new BusinessValidationException(errorMessage);
//
//        var response = exceptionHandler.handleBusinessValidationException(exception);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        Map<String, String> responseBody = response.getBody();
//        assertNotNull(responseBody);
//        assertEquals(errorMessage, responseBody.get("error"));
//        assertEquals("FAILED", responseBody.get("status"));
//    }
//
//    @Test
//    void handleValidationExceptions_ReturnsCorrectResponse() {
//        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
//        BindingResult bindingResult = mock(BindingResult.class);
//        FieldError fieldError = new FieldError("object", "field", "Field error message");
//
//        when(exception.getBindingResult()).thenReturn(bindingResult);
//        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));
//
//        var response = exceptionHandler.handleValidationExceptions(exception);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals("Field error message", response.getBody());
//    }
//
//    @Test
//    void handlePaymentProcessingException_ReturnsCorrectResponse() {
//        String errorMessage = "Payment failed";
//        PaymentProcessingException exception = new PaymentProcessingException(errorMessage);
//
//        var response = exceptionHandler.handlePaymentProcessingException(exception);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertNotNull(response.getBody());
//    }
//
//    @Test
//    void handleDataNotFoundException_ReturnsCorrectResponse() {
//        String errorMessage = "Data not found";
//        DataNotFoundException exception = new DataNotFoundException(errorMessage);
//
//        var response = exceptionHandler.handleDataNotFoundException(exception);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(errorMessage, response.getBody());
//    }
//}
