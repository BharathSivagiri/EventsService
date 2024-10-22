package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;
import com.ems.EventsService.model.EventsModel;

import com.ems.EventsService.utility.DateUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EventsMapper
{
    public Events toEntity(EventsModel eventsModel)
    {
        Events events = new Events();
    
        events.setEventName(eventsModel.getEventName());
        events.setEventDescription(eventsModel.getEventDescription());
        events.setEventDate(String.valueOf(DateUtils.convertToDate(eventsModel.getEventDate())));
        events.setEventLocation(eventsModel.getEventLocation());
        events.setEventCapacity(eventsModel.getEventCapacity());
        events.setEventFee(Double.parseDouble(eventsModel.getEventFee()));
        events.setEventStatus(EventStatus.valueOf(eventsModel.getEventStatus().toUpperCase()));
        events.setRecStatus(DBRecordStatus.valueOf(eventsModel.getRecStatus().toUpperCase()));
    
        return events;
    }

    public EventsModel toModel(Events events)
    {
        EventsModel eventsModel = new EventsModel();

        eventsModel.setEventId(String.valueOf(events.getEventId()));
        eventsModel.setEventName(events.getEventName());
        eventsModel.setEventDescription(events.getEventDescription());
        eventsModel.setEventDate(DateUtils.convertToString(LocalDate.parse(events.getEventDate())));
        eventsModel.setEventLocation(events.getEventLocation());
        eventsModel.setEventCapacity(events.getEventCapacity());
        eventsModel.setEventFee(String.valueOf(events.getEventFee()));
        eventsModel.setEventStatus(events.getEventStatus().name());
        eventsModel.setRecStatus(events.getRecStatus().name());

        return eventsModel;
    }
}
