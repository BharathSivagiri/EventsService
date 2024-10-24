package com.ems.EventsService.enums;

import com.ems.EventsService.utility.constants.ErrorMessages;
import lombok.Getter;

@Getter
public enum EventStatus
{
    OPENED("opened"),
    CLOSED("closed"),
    CANCELLED("cancelled");

    private final String eventStatus;

    EventStatus(String eventStatus)
    {
        this.eventStatus = eventStatus;
    }

    public static EventStatus fromString(String eventStatus)
    {
        for (EventStatus estatus : EventStatus.values())
        {
            if (estatus.getEventStatus().equalsIgnoreCase(eventStatus))
            {
                return estatus;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.INVALID_EVENT_STATUS);
    }
}