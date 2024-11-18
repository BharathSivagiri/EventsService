package com.ems.EventsService.model;

import lombok.Data;

@Data
public class ParticipantEventDTO
{
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String eventDate;
    private String eventLocation;
    private Integer eventCapacity;
    private String eventFee;
    private String eventStatus;
}

