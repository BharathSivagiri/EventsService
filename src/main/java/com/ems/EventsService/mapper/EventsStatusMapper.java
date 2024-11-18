package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.enums.DBRecordStatus;
import org.springframework.stereotype.Component;

@Component
public class EventsStatusMapper {

    public Events mapRecordStatus(Events event, DBRecordStatus status) {
        event.setRecStatus(status);
        return event;
    }
}
