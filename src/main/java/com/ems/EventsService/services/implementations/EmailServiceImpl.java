package com.ems.EventsService.services.implementations;

import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.services.EmailService;
import com.ems.EventsService.utility.constants.ErrorMessages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService
{

    @Autowired
    JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    String fromEmail;

    @Override
    public void sendHtmlMessage(String to, String subject, String htmlContent)
    {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        }
        catch (MessagingException e)
        {
            throw new BusinessValidationException(ErrorMessages.EMAIL_NOT_SENT);
        }
    }
}

