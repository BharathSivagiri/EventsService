package com.ems.EventsService.services.implementations;

import com.ems.EventsService.dto.PaymentRequestDTO;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;
import com.ems.EventsService.exceptions.custom.BasicValidationException;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.exceptions.custom.DataNotFoundException;
import com.ems.EventsService.mapper.*;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRegistrationRepository;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.repositories.UsersRepository;
import com.ems.EventsService.services.EventsService;
import com.ems.EventsService.utility.constants.ErrorMessages;
import com.ems.EventsService.utility.constants.AppConstants;

import com.ems.EventsService.validations.EventValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    EventsRegistrationMapper eventsRegistrationMapper;

    @Autowired
    PaymentClientService paymentClientService;

    @Autowired
    PaymentRequestMapper paymentRequestMapper;

    @Autowired
    EventsUpdateMapper eventsUpdateMapper;

    @Autowired
    EventsStatusMapper eventsStatusMapper;

    @Autowired
    PaymentAccountMapper paymentAccountMapper;

    @Autowired
    EventValidation eventValidation;

    @Autowired
    EventEmailService eventemailService;

    @Override
    public EventsModel createEvent(EventsModel eventsModel) {
        logger.info("Event creation for {} started", eventsModel.getEventName());
        eventValidation.validateEventCreation(eventsModel);
        Events events = eventsMapper.toEntity(eventsModel);
        Events savedEvent = eventsRepository.save(events);
        logger.info("Event creation completed for {}", eventsModel.getEventName());
        return eventsMapper.toModel(savedEvent);
    }

    @Transactional
    @Override
    public EventsModel updateEvent(Integer eventId, EventsModel eventsModel) {
        logger.info("Event update started for ID {}", eventId);
        Events existingEvent = getEventById(eventId);
        Map<String, String> oldValues = captureEventState(existingEvent);

        existingEvent = eventsUpdateMapper.mapForUpdate(existingEvent, eventsModel);
        Events updatedEvent = eventsRepository.save(existingEvent);

        processEventUpdateNotifications(updatedEvent, oldValues, eventsModel);

        logger.info("Event update completed for ID {}", eventId);
        return eventsMapper.toModel(updatedEvent);
    }

    @Override
    @Transactional
    public void deleteEvent(Integer eventId) {
        logger.info("Event deletion started for ID {}", eventId);
        Events event = getEventById(eventId);
        event = eventsStatusMapper.mapRecordStatus(event, DBRecordStatus.INACTIVE);
        eventsRepository.save(event);
        logger.info("Event deletion completed for ID {}", eventId);
    }

    @Override
    public List<?> getAllEvents(boolean isAdmin, String keyword, String dateA, String dateB, String status) {
        logger.info("Fetching events with filters - Admin access: {}, Status filter: {}", isAdmin, status);
        List<Events> events = fetchEventsWithFilters(dateA, dateB, keyword);
        List<?> result = mapEventsToResponse(events, isAdmin, status);
        validateEventResults(result);
        return result;
    }

    @Transactional
    public EventsRegistration registerForEvent(String transactionId, String eventId, String userId, String createdBy) {
        logger.info("Event registration started for event ID {} and user ID {}", eventId, userId);
        eventValidation.validateRegistration(eventId, userId);

        Events event = processEventCapacity(eventId);
        EventsRegistration registration = createRegistration(transactionId, eventId, userId, createdBy);

        Users user = getUserById(Integer.parseInt(userId));
        eventemailService.sendRegistrationEmail(user, event, registration.getId().toString());

        return registration;
    }

    @Transactional
    public EventsRegistration cancelEventRegistration(PaymentRequestDTO request) {
        logger.info("Cancelling registration for event ID {}", request.getEventId());
        EventsRegistration registration = getRegistration(request);
        Events event = getEventById(Integer.parseInt(request.getEventId()));

        processRefund(request);
        updateEventCapacity(event, true);

        return eventsRegistrationMapper.toCancelledRegistration(registration, request.getCreatedBy());
    }

    @Override
    public List<Map<String, Object>> getEventParticipants(Integer eventId) {
        logger.info("Fetching participants for event ID {}", eventId);
        List<Events> events = fetchEventsList(eventId);
        return mapParticipantsData(events);
    }

    @Transactional
    public EventsRegistration processEventRegistration(PaymentRequestDTO request) {
        logger.info("Processing event registration for user ID {}", request.getUserId());
        eventValidation.validateRegistration(request.getEventId(), request.getUserId());

        Users user = getUserById(Integer.parseInt(request.getUserId()));
        PaymentRequestDTO updatedRequest = paymentAccountMapper.mapUserAccountToPaymentRequest(request, user);

        Integer transactionId = paymentClientService.processPayment(updatedRequest);

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
        String targetDate = LocalDate.now().plusDays(2)
                .format(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));

        eventsRepository.findByEventDateAndRecStatus(targetDate, DBRecordStatus.ACTIVE)
                .forEach(this::processEventReminders);

        logger.info("Completed event reminder job");
    }


    // Helper methods
    private Events getEventById(Integer eventId) {
        return eventsRepository.findByEventIdAndRecStatus(eventId, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.EVENT_NOT_FOUND));
    }

    private Users getUserById(Integer userId) {
        return usersRepository.findByUserIdAndRecStatus(userId, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.USER_NOT_FOUND));
    }

    private Map<String, String> captureEventState(Events event) {
        return Map.of(
                "name", event.getEventName(),
                "location", event.getEventLocation(),
                "date", event.getEventDate(),
                "capacity", String.valueOf(event.getEventCapacity()),
                "fee", String.valueOf(event.getEventFee())
        );
    }

    private void processEventReminders(Events event) {
        eventsRegistrationRepository.findByEventIdAndRecordStatus(event.getEventId(), DBRecordStatus.ACTIVE)
                .forEach(registration -> {
                    Users user = getUserById(registration.getUserId());
                    eventemailService.sendReminderEmail(user, event, registration);
                });
    }

    private List<Events> fetchEventsWithFilters(String dateA, String dateB, String keyword) {
        if (dateA != null && dateB != null) {
            return eventsRepository.findByEventDateBetweenAndRecStatus(dateA, dateB, DBRecordStatus.ACTIVE);
        }
        return eventsRepository.findByRecStatusAndEventNameContainingIgnoreCaseOrEventLocationContainingIgnoreCase(
                DBRecordStatus.ACTIVE, keyword, keyword);
    }

    private void processEventUpdateNotifications(Events updatedEvent, Map<String, String> oldValues, EventsModel eventsModel) {
        List<EventsRegistration> registrations = eventsRegistrationRepository
                .findByEventIdAndRecordStatus(updatedEvent.getEventId(), DBRecordStatus.ACTIVE);

        if (isEventCancelled(eventsModel)) {
            registrations.forEach(reg -> sendCancellationNotification(reg, updatedEvent));
        } else {
            registrations.forEach(reg -> sendUpdateNotification(reg, updatedEvent, oldValues));
        }
    }

    private boolean isEventCancelled(EventsModel eventsModel) {
        return eventsModel.getEventStatus() != null &&
                EventStatus.fromString(eventsModel.getEventStatus()) == EventStatus.CANCELLED;
    }

    private void sendCancellationNotification(EventsRegistration registration, Events event) {
        Users user = getUserById(registration.getUserId());
        eventemailService.sendEventCancellationEmail(user, event);
    }

    private void sendUpdateNotification(EventsRegistration registration, Events event, Map<String, String> oldValues) {
        Users user = getUserById(registration.getUserId());
        eventemailService.sendEventUpdateEmail(user, event, oldValues);
    }

    private Events processEventCapacity(String eventId) {
        Events event = getEventById(Integer.parseInt(eventId));
        if (event.getEventCapacity() <= 0) {
            throw new BusinessValidationException(ErrorMessages.EVENT_FULL_CAPACITY);
        }
        updateEventCapacity(event, false);
        return event;
    }

    private void updateEventCapacity(Events event, boolean increase) {
        event.setEventCapacity(event.getEventCapacity() + (increase ? 1 : -1));
        eventsRepository.save(event);
    }

    private EventsRegistration createRegistration(String transactionId, String eventId, String userId, String createdBy) {
        EventsRegistration registration = eventsRegistrationMapper.toEntity(transactionId, eventId, userId, createdBy);
        return eventsRegistrationRepository.save(registration);
    }

    private void processRefund(PaymentRequestDTO request) {
        PaymentRequestDTO refundRequest = paymentRequestMapper.toRefundRequest(request);
        paymentClientService.processRefund(refundRequest);
    }

    private List<Map<String, Object>> mapParticipantsData(List<Events> events) {
        return events.stream()
                .map(this::createEventParticipantsMap)
                .collect(Collectors.toList());
    }

    private Map<String, Object> createEventParticipantsMap(Events event) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", event.getEventId());
        eventData.put("eventName", event.getEventName());
        eventData.put("Participants", getParticipantsList(event.getEventId()));
        return eventData;
    }

    private List<Map<String, Object>> getParticipantsList(Integer eventId) {
        return eventsRegistrationRepository
                .findByEventIdAndRecordStatus(eventId, DBRecordStatus.ACTIVE)
                .stream()
                .map(this::createParticipantMap)
                .collect(Collectors.toList());
    }

    private Map<String, Object> createParticipantMap(EventsRegistration registration) {
        Map<String, Object> participant = new HashMap<>();
        Users user = getUserById(registration.getUserId());
        participant.put("userId", user.getUserId());
        participant.put("username", user.getUsername());
        participant.put("registrationId", registration.getId());
        return participant;
    }

    private List<?> mapEventsToResponse(List<Events> events, boolean isAdmin, String status) {
        return events.stream()
                .filter(event -> filterByStatus(event, isAdmin, status))
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
                    filteredEvent.put("recordStatus", event.getRecStatus());
                    return filteredEvent;
                })
                .collect(Collectors.toList());
    }

    private boolean filterByStatus(Events event, boolean isAdmin, String status) {
        if (!isAdmin) {
            return event.getRecStatus() == DBRecordStatus.ACTIVE;
        }

        if (status == null) {
            return true; // Show all records for admin when no status filter
        }

        return event.getRecStatus() == DBRecordStatus.valueOf(status.toUpperCase());
    }

    private void validateEventResults(List<?> results) {
        if (results.isEmpty()) {
            throw new DataNotFoundException(ErrorMessages.NO_EVENTS_FOUND);
        }
    }

    private EventsRegistration getRegistration(PaymentRequestDTO request) {
        return eventsRegistrationRepository
                .findByEventIdAndUserIdAndRecordStatus(
                        Integer.parseInt(request.getEventId()),
                        Integer.parseInt(request.getUserId()),
                        DBRecordStatus.ACTIVE
                ).orElseThrow(() -> new BasicValidationException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    private List<Events> fetchEventsList(Integer eventId) {
        return Optional.ofNullable(eventId)
                .map(id -> Collections.singletonList(getEventById(id)))
                .orElseGet(() -> eventsRepository.findByRecStatus(DBRecordStatus.ACTIVE));
    }


}




