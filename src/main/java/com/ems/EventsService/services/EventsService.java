package com.ems.EventsService.services;

import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.model.EventsModel;

import java.util.List;
import java.util.Map;

public interface EventsService
{
    EventsModel createEvent(EventsModel eventsModel);

    EventsModel updateEvent(Integer eventId, EventsModel eventsModel);

    void deleteEvent(Integer eventId);

    List<?> getAllEvents(boolean isAdmin, String keyword);

    EventsRegistration registerForEvent(String transactionId, String eventId, String userId, String createdBy);

    List<Map<String, Object>> getEventParticipants(Integer eventId);



}
