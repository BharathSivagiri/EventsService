package com.ems.EventsService.services.implementations;

import com.ems.EventsService.dto.PaymentRequestDTO;
import com.ems.EventsService.entity.EmailTemplates;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;
import com.ems.EventsService.enums.RegistrationStatus;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.exceptions.custom.DataNotFoundException;
import com.ems.EventsService.exceptions.custom.DateInvalidException;
import com.ems.EventsService.mapper.EventsMapper;
import com.ems.EventsService.mapper.EventsRegistrationMapper;
import com.ems.EventsService.mapper.ParticipantEventDTOMapper;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRegistrationRepository;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.repositories.UsersRepository;
import com.ems.EventsService.services.EventsService;
import com.ems.EventsService.utility.DateUtils;
import com.ems.EventsService.utility.constants.ErrorMessages;
import com.ems.EventsService.utility.constants.AppConstants;
import com.ems.EventsService.repositories.EmailTemplatesRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService {
    private static final Logger logger = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    UsersRepository usersRepository;

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
    EmailServiceImpl emailService;

    @Autowired
    PaymentClientService paymentClientService;

    @Override
    public EventsModel createEvent(EventsModel eventsModel) {
        logger.info("Event creation for " + eventsModel.getEventName() + " started");
        if (eventsModel.getEventName() == null || eventsModel.getEventName().trim().isEmpty()) {
            throw new BusinessValidationException(ErrorMessages.EVENT_NAME_EMPTY);
        }
        if (eventsRepository.existsByEventNameAndRecStatus(eventsModel.getEventName(), DBRecordStatus.ACTIVE)) {
            throw new BusinessValidationException(ErrorMessages.EVENT_NAME_EXISTS);
        }
        if (eventsModel.getEventDate() == null) {
            throw new BusinessValidationException(ErrorMessages.EVENT_DATE_NULL);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
        LocalDate eventDate = LocalDate.parse(eventsModel.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDate.now())) {
            throw new BusinessValidationException(ErrorMessages.EVENT_DATE_PAST);
        }
        Events events = eventsMapper.toEntity(eventsModel);
        events.setCreatedBy(AppConstants.ADMIN_ROLE);
        events.setUpdatedBy(AppConstants.ADMIN_ROLE);
        events.setCreatedDate(String.valueOf(LocalDate.now()));
        events.setUpdatedDate(String.valueOf(LocalDate.now()));
        Events savedEvent = eventsRepository.save(events);
        logger.info("Event creation for " + eventsModel.getEventName() + " completed");
        return eventsMapper.toModel(savedEvent);
    }

    @Transactional
    @Override
    public EventsModel updateEvent(Integer eventId, EventsModel eventsModel) {
        logger.info("Event update for ID {} started", eventId);

        Events existingEvent = eventsRepository.findById(eventId)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.EVENT_NOT_FOUND));

        Map<String, String> oldValues = Map.of(
                "name", existingEvent.getEventName(),
                "location", existingEvent.getEventLocation(),
                "date", existingEvent.getEventDate(),
                "capacity", String.valueOf(existingEvent.getEventCapacity()),
                "fee", String.valueOf(existingEvent.getEventFee())
        );

        Optional.ofNullable(eventsModel.getEventName()).ifPresent(existingEvent::setEventName);
        Optional.ofNullable(eventsModel.getEventDescription()).ifPresent(existingEvent::setEventDescription);
        Optional.ofNullable(eventsModel.getEventLocation()).ifPresent(existingEvent::setEventLocation);
        Optional.ofNullable(eventsModel.getEventCapacity()).ifPresent(existingEvent::setEventCapacity);
        Optional.ofNullable(eventsModel.getEventFee()).ifPresent(fee -> existingEvent.setEventFee(Double.parseDouble(fee)));
        Optional.ofNullable(eventsModel.getEventStatus()).ifPresent(status -> existingEvent.setEventStatus(EventStatus.fromString(status)));
        Optional.ofNullable(eventsModel.getRecStatus()).ifPresent(status -> existingEvent.setRecStatus(DBRecordStatus.fromString(status)));

        try {
            existingEvent.setEventDate(String.valueOf(DateUtils.convertToDate(eventsModel.getEventDate())));
        } catch (DateTimeParseException e) {
            throw new DateInvalidException(ErrorMessages.INVALID_DATE_FORMAT);
        }

        String currentDate = String.valueOf(LocalDate.now());
        existingEvent.setCreatedBy(AppConstants.ADMIN_ROLE);
        existingEvent.setUpdatedBy(AppConstants.ADMIN_ROLE);
        existingEvent.setCreatedDate(currentDate);
        existingEvent.setUpdatedDate(currentDate);

        Events updatedEvent = eventsRepository.save(existingEvent);
        logger.info("Event update for ID {} completed", eventId);

        List<EventsRegistration> registrations = eventsRegistrationRepository.findByEventIdAndRecordStatus(eventId, DBRecordStatus.ACTIVE);

        if (eventsModel.getEventStatus() != null &&
                EventStatus.fromString(eventsModel.getEventStatus()) == EventStatus.CANCELLED) {

            registrations.stream()
                    .map(reg -> usersRepository.findById(reg.getUserId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(user -> {
                        EmailTemplates template = emailTemplatesRepository
                                .findByTemplateNameAndRecStatus("EVENT_CANCELLED", DBRecordStatus.ACTIVE)
                                .orElseThrow(() -> new RuntimeException(ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND));

                        String emailContent = template.getTemplateCode()
                                .replace("{userName}", user.getUsername())
                                .replace("{eventName}", updatedEvent.getEventName())
                                .replace("{eventLocation}", updatedEvent.getEventLocation())
                                .replace("{eventDate}", updatedEvent.getEventDate());

                        emailService.sendHtmlMessage(
                                user.getEmail(),
                                "Event Cancelled: " + updatedEvent.getEventName(),
                                emailContent
                        );
                    });
        } else {
            registrations.stream()
                    .map(reg -> usersRepository.findById(reg.getUserId()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(user -> emailService.sendHtmlMessage(
                            user.getEmail(),
                            "Event Update: " + updatedEvent.getEventName(),
                            getUpdatedEventEmailContent(updatedEvent,
                                    oldValues.get("name"),
                                    oldValues.get("location"),
                                    oldValues.get("date"),
                                    oldValues.get("capacity"),
                                    oldValues.get("fee"),
                                    user.getUsername())
                    ));
        }

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
    public List<?> getAllEvents(boolean isAdmin, String keyword, String dateA, String dateB) {
        logger.info("Fetching all events started");

        List<Events> events;
        if (dateA != null && dateB != null) {
            events = eventsRepository.findByEventDateBetweenAndRecStatus(
                    dateA, dateB, DBRecordStatus.ACTIVE);
        } else {
            events = eventsRepository.findByRecStatusAndEventNameContainingIgnoreCaseOrEventLocationContainingIgnoreCase(
                    DBRecordStatus.ACTIVE, keyword, keyword);
        }

        if (events.isEmpty()) {
            throw new DataNotFoundException(ErrorMessages.NO_EVENTS_FOUND);
        }

        List<?> result = events.stream()
                .map(event -> {
                    Map<String, Object> filteredEvent = new HashMap<>();
                    filteredEvent.put("eventId", event.getEventId());
                    filteredEvent.put("eventName", event.getEventName());
                    filteredEvent.put("eventDescription", event.getEventDescription());
                    filteredEvent.put("eventDate", event.getEventDate());
                    filteredEvent.put("eventLocation", event.getEventLocation());
                    filteredEvent.put("eventCapacity", event.getEventCapacity());
                    filteredEvent.put("eventFee", event.getEventFee());
                    filteredEvent.put("eventStatus", event.getEventStatus());
                    return filteredEvent;
                })
                .collect(Collectors.toList());
        if (result.isEmpty()) {
            throw new DataNotFoundException(ErrorMessages.NO_EVENTS_FOUND);
        }

        logger.info("Fetching all events completed");
        return result;
    }


    @Transactional
    public EventsRegistration registerForEvent(String transactionId, String eventId, String userId, String createdBy) {
        logger.info("Registering for event with ID {} started", eventId);
        List<EventsRegistration> existingRegistrations = eventsRegistrationRepository
                .findByEventIdAndRecordStatus(Integer.parseInt(eventId), DBRecordStatus.ACTIVE);

        boolean isAlreadyRegistered = existingRegistrations.stream()
                .anyMatch(reg -> reg.getUserId().equals(Integer.parseInt(userId)));

        if (isAlreadyRegistered) {
            throw new BusinessValidationException(ErrorMessages.EVENT_ALREADY_REGISTERED);
        }

        Events event = eventsRepository.findById(Integer.parseInt(eventId))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EVENT_NOT_FOUND));

        if (event.getEventCapacity() <= 0) {
            throw new BusinessValidationException(ErrorMessages.EVENT_FULL_CAPACITY);
        }

        event.setEventCapacity(event.getEventCapacity() - 1);
        eventsRepository.save(event);

        EventsRegistration registration = eventsRegistrationMapper.toEntity(transactionId, eventId, userId, createdBy);
        EventsRegistration savedRegistration = eventsRegistrationRepository.save(registration);
        logger.info("Registered for event with ID {} completed", eventId);

        logger.info("Sending registration success email to user with ID {} started", userId);
        var user = usersRepository.findById(Integer.parseInt(userId))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.USER_NOT_FOUND));

        EmailTemplates template = emailTemplatesRepository
                .findByTemplateNameAndRecStatus("REGISTRATION_SUCCESS", DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND));

        String emailContent = template.getTemplateCode()
                .replace("{userName}", user.getUsername())
                .replace("{eventName}", event.getEventName())
                .replace("{eventLocation}", event.getEventLocation())
                .replace("{eventDate}", event.getEventDate())
                .replace("{registrationId}", savedRegistration.getId().toString());

        emailService.sendHtmlMessage(
                user.getEmail(),
                "Event Registration Confirmation: " + event.getEventName(),
                emailContent
        );
        logger.info("Sending registration success email to user with ID {} completed", userId);
        return savedRegistration;
    }

    @Transactional
    public EventsRegistration cancelEventRegistration(PaymentRequestDTO request) {
        logger.info("Cancelling registration for event with ID {} started", request.getEventId());
        EventsRegistration registration = eventsRegistrationRepository
                .findByEventIdAndUserIdAndRecordStatus(
                        Integer.parseInt(request.getEventId()),
                        Integer.parseInt(request.getUserId()),
                        DBRecordStatus.ACTIVE
                ).orElseThrow(() -> new DataNotFoundException(ErrorMessages.REGISTRATION_NOT_FOUND));

        Events event = eventsRepository.findById(Integer.parseInt(request.getEventId()))
                .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EVENT_NOT_FOUND));

        event.setEventCapacity(event.getEventCapacity() + 1);
        eventsRepository.save(event);

        request.setTransactionType("CREDIT");
        request.setAmountPaid(String.valueOf(event.getEventFee()));
        Integer refundTransactionId = paymentClientService.processRefund(request);

        registration.setRegistrationStatus(RegistrationStatus.CANCELLED);
        registration.setRecordStatus(DBRecordStatus.INACTIVE);
        registration.setLastUpdatedDate(LocalDateTime.now().toString());
        registration.setLastUpdatedBy(request.getCreatedBy());

        EventsRegistration cancelledRegistration = eventsRegistrationRepository.save(registration);
        logger.info("Cancelling registration for event with ID {} completed", request.getEventId());
        return cancelledRegistration;
    }


    private String getUpdatedEventEmailContent(Events updatedEvent, String oldName,
                                               String oldLocation, String oldDate, String oldCapacity, String oldFee, String userName) {
        EmailTemplates template = emailTemplatesRepository.findByTemplateNameAndRecStatus("EVENT_UPDATED", DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND));

        String emailContent = template.getTemplateCode();

        StringBuilder updatedDetails = new StringBuilder();
        updatedDetails.append("<ul>");

        if (!oldName.equals(updatedEvent.getEventName())) {
            updatedDetails.append("<li>Event Name: ").append(oldName)
                    .append(" → ").append(updatedEvent.getEventName()).append("</li>");
        }

        if (!oldLocation.equals(updatedEvent.getEventLocation())) {
            updatedDetails.append("<li>Location: ").append(oldLocation)
                    .append(" → ").append(updatedEvent.getEventLocation()).append("</li>");
        }

        if (!oldDate.equals(updatedEvent.getEventDate())) {
            updatedDetails.append("<li>Date: ").append(oldDate)
                    .append(" → ").append(updatedEvent.getEventDate()).append("</li>");
        }

        if (!oldCapacity.equals(String.valueOf(updatedEvent.getEventCapacity()))) {
            updatedDetails.append("<li>Capacity: ").append(oldCapacity)
                    .append(" → ").append(updatedEvent.getEventCapacity()).append("</li>");
        }

        if (!oldFee.equals(String.valueOf(updatedEvent.getEventFee()))) {
            updatedDetails.append("<li>Fee: ").append(oldFee)
                    .append(" → ").append(updatedEvent.getEventFee()).append("</li>");
        }

        updatedDetails.append("</ul>");

        return emailContent.replace("{userName}", userName)
                .replace("{eventName}", updatedEvent.getEventName())
                .replace("{eventLocation}", updatedEvent.getEventLocation())
                .replace("{eventDate}", updatedEvent.getEventDate())
                .replace("{eventDescription}", updatedEvent.getEventDescription())
                .replace("{eventCapacity}", String.valueOf(updatedEvent.getEventCapacity()))
                .replace("{eventFee}", String.valueOf(updatedEvent.getEventFee()))
                .replace("{updatedDetails}", updatedDetails.toString());
    }
    @Override
    public List<Map<String, Object>> getEventParticipants(Integer eventId) {
        logger.info("Fetching participants for event with ID {} started", eventId);
        List<Events> events;
        try {
            if (eventId != null) {
                events = Collections.singletonList(eventsRepository.findById(eventId)
                        .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EVENT_NOT_FOUND)));
            } else {
                events = eventsRepository.findByRecStatus(DBRecordStatus.ACTIVE);
            }

            return events.stream().map(event ->
            {
                Map<String, Object> eventData = new HashMap<>();
                eventData.put("eventId", event.getEventId());
                eventData.put("eventName", event.getEventName());

                List<Map<String, Object>> participants = eventsRegistrationRepository
                        .findByEventIdAndRecordStatus(event.getEventId(), DBRecordStatus.ACTIVE)
                        .stream()
                        .map(registration -> {
                            Map<String, Object> participant = new HashMap<>();
                            var user = usersRepository.findById(registration.getUserId())
                                    .orElseThrow(() -> new DataNotFoundException(ErrorMessages.USER_NOT_FOUND));

                            participant.put("userId", user.getUserId());
                            participant.put("username", user.getUsername());
                            participant.put("registrationId", registration.getId());
                            return participant;
                        })
                        .collect(Collectors.toList());

                eventData.put("Participants", participants);
                logger.info("Fetched participants for event with ID {} successfully", eventId);
                return eventData;
            }).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new BusinessValidationException(ErrorMessages.INVALID_EVENT_ID);
        } catch (Exception e) {
            throw new BusinessValidationException(ErrorMessages.EVENT_RETRIEVAL_ERROR);
        }
    }

    @Transactional
    public EventsRegistration processEventRegistration(PaymentRequestDTO request)  throws BusinessValidationException {
        boolean isAlreadyRegistered = eventsRegistrationRepository
                .findByEventIdAndRecordStatus(Integer.parseInt(request.getEventId()), DBRecordStatus.ACTIVE)
                .stream()
                .anyMatch(reg -> reg.getUserId().equals(Integer.parseInt(request.getUserId())));

        if (isAlreadyRegistered) {
            throw new BusinessValidationException(ErrorMessages.USER_ALREADY_REGISTERED);
        }

        Integer transactionId = paymentClientService.processPayment(request);

        return registerForEvent(
                transactionId.toString(),
                request.getEventId(),
                request.getUserId(),
                request.getCreatedBy()
        );
    }

    @ConditionalOnProperty(value = "scheduler.enabled", havingValue = "true", matchIfMissing = false)
    @Scheduled(cron = "${scheduler.cron}")
    public void sendEventReminders() {
        logger.info("Starting event reminder job");

        LocalDate twoDaysFromNow = LocalDate.now().plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
        String targetDate = twoDaysFromNow.format(formatter);

        List<Events> upcomingEvents = eventsRepository.findByEventDateAndRecStatus(
                targetDate,
                DBRecordStatus.ACTIVE
        );

        for (Events event : upcomingEvents) {
            List<EventsRegistration> registrations = eventsRegistrationRepository
                    .findByEventIdAndRecordStatus(event.getEventId(), DBRecordStatus.ACTIVE);

            for (EventsRegistration registration : registrations) {
                var user = usersRepository.findById(registration.getUserId())
                        .orElseThrow(() -> new DataNotFoundException(ErrorMessages.USER_NOT_FOUND));

                EmailTemplates template = emailTemplatesRepository
                        .findByTemplateNameAndRecStatus("EVENT_NOTIFICATION", DBRecordStatus.ACTIVE)
                        .orElseThrow(() -> new RuntimeException(ErrorMessages.EMAIL_TEMPLATE_NOT_FOUND));

                String emailContent = template.getTemplateCode()
                        .replace("{userName}", user.getUsername())
                        .replace("{eventName}", event.getEventName())
                        .replace("{eventLocation}", event.getEventLocation())
                        .replace("{eventDate}", event.getEventDate())
                        .replace("{registrationId}", registration.getId().toString());

                emailService.sendHtmlMessage(
                        user.getEmail(),
                        "Upcoming Event Reminder: " + event.getEventName(),
                        emailContent
                );

                logger.info("Sent reminder for event {} to user {}", event.getEventId(), user.getUserId());
            }
        }
        logger.info("Completed event reminder job");
    }
}



