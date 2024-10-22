package com.ems.EventsService.services.implementations;

import com.ems.EventsService.entity.EmailTemplates;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.exceptions.custom.DataNotFoundException;
import com.ems.EventsService.exceptions.custom.DateInvalidException;
import com.ems.EventsService.mapper.EventsMapper;
import com.ems.EventsService.mapper.EventsRegistrationMapper;
import com.ems.EventsService.mapper.ParticipantEventDTOMapper;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRegistrationRepository;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.services.EventsService;
import com.ems.EventsService.utility.DateUtils;
import com.ems.EventsService.utility.constants.ErrorMessages;
import com.ems.EventsService.repositories.EmailTemplatesRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService
{
    private static final Logger logger = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    EventsRegistrationRepository eventsRegistrationRepository;

    @Autowired
    EventsMapper eventsMapper;

    @Autowired
    ParticipantEventDTOMapper participantEventDTOMapper;

    @Autowired
    EventsRegistrationMapper eventsRegistrationMapper;

    @Autowired
    EmailTemplatesRepository emailTemplatesRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine tempEngine;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public EventsModel createEvent(EventsModel eventsModel)
    {
        logger.info("Event creation for " + eventsModel.getEventName() + " started");
        if (eventsModel.getEventName() == null || eventsModel.getEventName().trim().isEmpty()) {
            throw new BusinessValidationException(ErrorMessages.EVENT_NAME_EMPTY);
        }
        if (eventsRepository.existsByEventName(eventsModel.getEventName())) {
            throw new BusinessValidationException(ErrorMessages.EVENT_NAME_EXISTS);
        }
        if (eventsModel.getEventDate() == null) {
            throw new BusinessValidationException(ErrorMessages.EVENT_DATE_NULL);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eventDate = LocalDate.parse(eventsModel.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDate.now())) {
            throw new BusinessValidationException(ErrorMessages.EVENT_DATE_PAST);
        }
        Events events = eventsMapper.toEntity(eventsModel);
        events.setCreatedBy("Admin");
        events.setUpdatedBy("Admin");
        events.setCreatedDate(String.valueOf(LocalDate.now()));
        events.setUpdatedDate(String.valueOf(LocalDate.now()));
        Events savedEvent = eventsRepository.save(events);
        logger.info("Event creation for " + eventsModel.getEventName() + " completed");
        return eventsMapper.toModel(savedEvent);
    }
    @Transactional
    @Override
    public EventsModel updateEvent(Integer eventId, EventsModel eventsModel)
    {
        logger.info("Event update for ID {} started", eventId);

        Events existingEvent = eventsRepository.findById(eventId)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.EVENT_NOT_FOUND));

        if (eventsModel.getEventName() != null) existingEvent.setEventName(eventsModel.getEventName());
        if (eventsModel.getEventDescription() != null) existingEvent.setEventDescription(eventsModel.getEventDescription());
        if (eventsModel.getEventLocation() != null) existingEvent.setEventLocation(eventsModel.getEventLocation());
        if (eventsModel.getEventCapacity() != null) existingEvent.setEventCapacity(eventsModel.getEventCapacity());
        if (eventsModel.getEventFee() != null) existingEvent.setEventFee(Double.parseDouble(eventsModel.getEventFee()));

        try {
            existingEvent.setEventDate(String.valueOf(DateUtils.convertToDate(eventsModel.getEventDate())));
        } catch (DateTimeParseException e)
        {
            throw new DateInvalidException(ErrorMessages.INVALID_DATE_FORMAT);
        }

        if (eventsModel.getEventStatus() != null) {
            existingEvent.setEventStatus(EventStatus.fromString(eventsModel.getEventStatus()));
        }

        existingEvent.setCreatedBy("Admin");
        existingEvent.setUpdatedBy("Admin");
        existingEvent.setCreatedDate(String.valueOf(LocalDate.now()));
        existingEvent.setUpdatedDate(String.valueOf(LocalDate.now()));

        Events updatedEvent = eventsRepository.save(existingEvent);
        logger.info("Event update for ID {} completed", eventId);

        return eventsMapper.toModel(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Integer eventId) {
        logger.info("Soft delete for event with ID {} started", eventId);

        Events event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.EVENT_NOT_FOUND));

        event.setRecStatus(DBRecordStatus.INACTIVE);

        eventsRepository.save(event);

        logger.info("Soft delete for event with ID {} completed", eventId);
    }

    @Override
    public List<?> getAllEvents(boolean isAdmin, String keyword)
    {
        logger.info("Fetching all events started");
        List<Events> events = isAdmin
                ? eventsRepository.findByEventNameOrEventLocationContainingIgnoreCase(keyword, keyword)
                : eventsRepository.findByEventNameOrEventLocationContainingIgnoreCaseAndRecStatus(keyword, keyword, DBRecordStatus.ACTIVE);

        List<?> result;
        if (isAdmin) {
            result = events.stream().map(eventsMapper::toModel).collect(Collectors.toList());
        } else {
            result = events.stream().map(participantEventDTOMapper::toDTO).collect(Collectors.toList());
        }
        logger.info("Fetching all events completed");
        return result;
    }

    @Transactional
    public EventsRegistration registerForEvent(String transactionId, String eventId, String userId, String createdBy) {
        Events event = eventsRepository.findById(Integer.parseInt(eventId))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EVENT_NOT_FOUND));

        if (event.getEventCapacity() <= 0) {
            throw new BusinessValidationException(ErrorMessages.EVENT_FULL_CAPACITY);
        }

        event.setEventCapacity(event.getEventCapacity() - 1);
        eventsRepository.save(event);

        EventsRegistration registration = eventsRegistrationMapper.toEntity(transactionId, eventId, userId, createdBy);
        return eventsRegistrationRepository.save(registration);
    }

    public String getUpdatedEventEmailContent(EventsModel updatedEvent, String userName) {
        EmailTemplates template = emailTemplatesRepository.findByTemplateName("EVENT_UPDATED")
                .orElseThrow(() -> new RuntimeException("Email template not found"));

        String emailContent = template.getTemplateCode();

        emailContent = emailContent.replace("{userName}", userName)
                .replace("{eventName}", updatedEvent.getEventName())
                .replace("{updatedDetails}", generateUpdatedDetailsString(updatedEvent));

        return emailContent;
    }

    private String generateUpdatedDetailsString(EventsModel updatedEvent) {
        StringBuilder details = new StringBuilder();
        details.append("Date: ").append(updatedEvent.getEventDate()).append("<br>");
        details.append("Location: ").append(updatedEvent.getEventLocation());

        return details.toString();
    }

}



