package com.ems.EventsService.services;

import com.ems.EventsService.model.EventsModel;

import java.util.List;

public interface EventsService
{
    EventsModel createEvent(EventsModel eventsModel);

    EventsModel updateEvent(Integer eventId, EventsModel eventsModel);

    void deleteEvent(Integer eventId);

    List<?> getAllEvents(boolean isAdmin, String keyword);

}
