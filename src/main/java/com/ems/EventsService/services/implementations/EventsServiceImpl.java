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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService
{
    private static final Logger logger = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Autowired
    EventsRepository eventsRepository;

    @Autowired
    UsersRepository  usersRepository;

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

    @Override
    public EventsModel createEvent(EventsModel eventsModel)
    {
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
                "date", existingEvent.getEventDate()
        );

        Optional.ofNullable(eventsModel.getEventName()).ifPresent(existingEvent::setEventName);
        Optional.ofNullable(eventsModel.getEventDescription()).ifPresent(existingEvent::setEventDescription);
        Optional.ofNullable(eventsModel.getEventLocation()).ifPresent(existingEvent::setEventLocation);
        Optional.ofNullable(eventsModel.getEventCapacity()).ifPresent(existingEvent::setEventCapacity);
        Optional.ofNullable(eventsModel.getEventFee()).ifPresent(fee -> existingEvent.setEventFee(Double.parseDouble(fee)));
        Optional.ofNullable(eventsModel.getEventStatus()).ifPresent(status -> existingEvent.setEventStatus(EventStatus.fromString(status)));

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

        eventsRegistrationRepository.findByEventIdAndRecordStatus(eventId, DBRecordStatus.ACTIVE).stream()
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
                                user.getUsername())
                ));

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

        List<Events> events;
            events = eventsRepository.findByEventNameOrEventLocationContainingIgnoreCaseAndRecStatus(keyword, keyword, DBRecordStatus.ACTIVE);

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
        List<EventsRegistration> existingRegistrations = eventsRegistrationRepository
                .findByEventIdAndRecordStatus(Integer.parseInt(eventId), DBRecordStatus.ACTIVE);

        boolean isAlreadyRegistered = existingRegistrations.stream()
                .anyMatch(reg -> reg.getUserId().equals(Integer.parseInt(userId)));

        if (isAlreadyRegistered) {
            throw new BusinessValidationException("User is already registered for this event");
        }

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

    private String getUpdatedEventEmailContent(Events updatedEvent, String oldName,
                                               String oldLocation, String oldDate, String userName)
    {
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

        updatedDetails.append("</ul>");

        return emailContent.replace("{userName}", userName)
                .replace("{eventName}", updatedEvent.getEventName())
                .replace("{eventLocation}", updatedEvent.getEventLocation())
                .replace("{eventDate}", updatedEvent.getEventDate())
                .replace("{updatedDetails}", updatedDetails.toString());
    }

    @Override
    public List<Map<String, Object>> getEventParticipants(Integer eventId) {
        List<Events> events;
        try {
            if (eventId != null) {
                events = Collections.singletonList(eventsRepository.findById(eventId)
                        .orElseThrow(() -> new DataNotFoundException(ErrorMessages.EVENT_NOT_FOUND)));
            } else {
                events = eventsRepository.findByRecStatus(DBRecordStatus.ACTIVE);
            }

            return events.stream().map(event -> {
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
                return eventData;
            }).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new BusinessValidationException(ErrorMessages.INVALID_EVENT_ID);
        } catch (Exception e) {
            throw new BusinessValidationException(ErrorMessages.EVENT_RETRIEVAL_ERROR);
        }
    }
}



