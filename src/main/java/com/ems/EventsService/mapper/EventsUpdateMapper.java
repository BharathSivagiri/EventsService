package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.utility.constants.AppConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class EventsUpdateMapper {

    public Events mapForUpdate(Events existingEvent, EventsModel eventsModel) {
        Optional.ofNullable(eventsModel.getEventName()).ifPresent(existingEvent::setEventName);
        Optional.ofNullable(eventsModel.getEventDescription()).ifPresent(existingEvent::setEventDescription);
        Optional.ofNullable(eventsModel.getEventLocation()).ifPresent(existingEvent::setEventLocation);
        Optional.ofNullable(eventsModel.getEventCapacity()).ifPresent(existingEvent::setEventCapacity);
        Optional.ofNullable(eventsModel.getEventFee()).ifPresent(fee -> existingEvent.setEventFee(Double.parseDouble(fee)));
        Optional.ofNullable(eventsModel.getEventStatus()).ifPresent(status -> existingEvent.setEventStatus(EventStatus.fromString(status)));
        Optional.ofNullable(eventsModel.getRecStatus()).ifPresent(status -> existingEvent.setRecStatus(DBRecordStatus.fromString(status)));

        updateAuditFields(existingEvent);
        return existingEvent;
    }

    private void updateAuditFields(Events event) {
        String currentDate = String.valueOf(LocalDate.now());
        event.setCreatedBy(AppConstants.ADMIN_ROLE);
        event.setUpdatedBy(AppConstants.ADMIN_ROLE);
        event.setCreatedDate(currentDate);
        event.setUpdatedDate(currentDate);
    }
}