package com.ems.EventsService.services.implementations;

import com.ems.EventsService.entity.EmailTemplates;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.exceptions.custom.DataNotFoundException;
import com.ems.EventsService.repositories.EmailTemplatesRepository;
import com.ems.EventsService.utility.constants.ErrorMessages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventEmailService {
    private final EmailTemplatesRepository emailTemplatesRepository;
    private final EmailServiceImpl emailService;
    private final Logger logger = LoggerFactory.getLogger(EventEmailService.class);

    public void sendRegistrationEmail(Users user, Events event, String registrationId) {
        EmailTemplates template = getEmailTemplate("REGISTRATION_SUCCESS");
        String emailContent = template.getTemplateCode()
                .replace("{userName}", user.getUsername())
                .replace("{eventName}", event.getEventName())
                .replace("{eventLocation}", event.getEventLocation())
                .replace("{eventDate}", event.getEventDate())
                .replace("{registrationId}", registrationId);

        sendEmail(user.getEmail(), "Event Registration Confirmation: " + event.getEventName(), emailContent);
    }

    public void sendEventUpdateEmail(Users user, Events event, Map<String, String> oldValues) {
        EmailTemplates template = getEmailTemplate("EVENT_UPDATED");
        String emailContent = template.getTemplateCode()
                .replace("{userName}", user.getUsername())
                .replace("{eventName}", event.getEventName())
                .replace("{eventLocation}", event.getEventLocation())
                .replace("{eventDate}", event.getEventDate());

        for (Map.Entry<String, String> entry : oldValues.entrySet()) {
            emailContent = emailContent.replace("{old" + entry.getKey() + "}", entry.getValue());
        }

        sendEmail(user.getEmail(), "Event Update: " + event.getEventName(), emailContent);
    }

    public void sendEventCancellationEmail(Users user, Events event) {
        EmailTemplates template = getEmailTemplate("EVENT_CANCELLED");
        String emailContent = template.getTemplateCode()
                .replace("{userName}", user.getUsername())
                .replace("{eventName}", event.getEventName())
                .replace("{eventLocation}", event.getEventLocation())
                .replace("{eventDate}", event.getEventDate());

        sendEmail(user.getEmail(), "Event Cancelled: " + event.getEventName(), emailContent);
    }

    private EmailTemplates getEmailTemplate(String templateName) {
        return emailTemplatesRepository
                .findByTemplateNameAndRecStatus(templateName, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND));
    }

    public void sendReminderEmail(Users user, Events event, EventsRegistration registration) {
        EmailTemplates template = getEmailTemplate("EVENT_NOTIFICATION");
        String emailContent = template.getTemplateCode()
                .replace("{userName}", user.getUsername())
                .replace("{eventName}", event.getEventName())
                .replace("{eventLocation}", event.getEventLocation())
                .replace("{eventDate}", event.getEventDate())
                .replace("{registrationId}", registration.getId().toString());

        sendEmail(
                user.getEmail(),
                "Upcoming Event Reminder: " + event.getEventName(),
                emailContent
        );
    }


    private void sendEmail(String to, String subject, String content) {
        emailService.sendHtmlMessage(to, subject, content);
        logger.info("Email sent to {} with subject {}", to, subject);
    }
}
