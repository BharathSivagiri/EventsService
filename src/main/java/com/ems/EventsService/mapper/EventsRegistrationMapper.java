package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.RegistrationStatus;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventsRegistrationMapper
{
    public EventsRegistration toEntity(String transactionId, String eventId, String userId, String createdBy) {
        EventsRegistration registration = new EventsRegistration();
        registration.setEventId(Integer.parseInt(eventId));
        registration.setUserId(Integer.parseInt(userId));
        registration.setTransactionId(transactionId);
        registration.setRegistrationStatus(RegistrationStatus.REGISTERED);
        registration.setCreatedBy(createdBy);
        registration.setCreatedDate(LocalDateTime.now().toString());
        registration.setRecordStatus(DBRecordStatus.ACTIVE);
        registration.setLastUpdatedDate(LocalDateTime.now().toString());
        registration.setLastUpdatedBy(createdBy);
        return registration;
    }
}

