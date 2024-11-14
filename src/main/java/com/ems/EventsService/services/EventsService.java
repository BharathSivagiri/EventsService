package com.ems.EventsService.services;

import com.ems.EventsService.model.PaymentRequestDTO;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.model.EventsModel;

import java.util.List;
import java.util.Map;

public interface EventsService {
    EventsModel createEvent(EventsModel eventsModel);

    EventsModel updateEvent(Integer eventId, EventsModel eventsModel);

    void deleteEvent(Integer eventId);

    List<?> getAllEvents(boolean isAdmin, String keyword, String dateA, String dateB, String status);

    EventsRegistration registerForEvent(String transactionId, String eventId, String userId, String createdBy);

    List<Map<String, Object>> getEventParticipants(Integer eventId);

    EventsRegistration cancelEventRegistration(PaymentRequestDTO request);

    EventsRegistration processEventRegistration(PaymentRequestDTO request)  throws BusinessValidationException;

}
