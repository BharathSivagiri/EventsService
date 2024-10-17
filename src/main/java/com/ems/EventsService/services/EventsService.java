package com.ems.EventsService.services;

import com.ems.EventsService.model.EventsModel;

public interface EventsService
{
    EventsModel createEvent(EventsModel eventsModel);

//    EventsModel updateEvent(String eventName, String eventDescription, String eventDate, String eventLocation, String eventTime, String eventDuration, String eventType, String eventStatus);
//
//    EventsModel deleteEvent(int eventId);
//
//    EventsModel getEvent(int eventId);
//
//    EventsModel getAllEvents();
}
