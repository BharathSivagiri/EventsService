//package com.ems.EventsService.services.implementations;
//
//import com.ems.EventsService.exceptions.custom.BusinessValidationException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.MailSendException;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.test.util.ReflectionTestUtils;
//import jakarta.mail.Session;
//import jakarta.mail.internet.MimeMessage;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class EmailServiceImplTest {
//
//    @Mock
//    private JavaMailSender emailSender;
//
//    @InjectMocks
//    private EmailServiceImpl emailService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com");
//        MimeMessage mimeMessage = new MimeMessage((Session)null);
//        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
//    }
//
//    @Test
//    void sendHtmlMessage_Success() {
//        String to = "recipient@example.com";
//        String subject = "Test Subject";
//        String htmlContent = "<h1>Test Content</h1>";
//
//        doNothing().when(emailSender).send(any(MimeMessage.class));
//
//        assertDoesNotThrow(() ->
//                emailService.sendHtmlMessage(to, subject, htmlContent)
//        );
//
//        verify(emailSender, times(1)).createMimeMessage();
//        verify(emailSender, times(1)).send(any(MimeMessage.class));
//    }
//
//    @Test
//    void sendHtmlMessage_ThrowsException() {
//        String to = "recipient@example.com";
//        String subject = "Test Subject";
//        String htmlContent = "<h1>Test Content</h1>";
//
//        doThrow(new BusinessValidationException("Mail error")).when(emailSender).send(any(MimeMessage.class));
//
//        BusinessValidationException exception = assertThrows(
//                BusinessValidationException.class,
//                () -> emailService.sendHtmlMessage(to, subject, htmlContent)
//        );
//
//        verify(emailSender, times(1)).createMimeMessage();
//        verify(emailSender, times(1)).send(any(MimeMessage.class));
//    }
//}
