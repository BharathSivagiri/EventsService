package com.ems.EventsService.mapper;

import com.ems.EventsService.dto.ParticipantEventDTO;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.utility.constants.AppConstants;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ParticipantEventDTOMapper
{
    public ParticipantEventDTO toDTO(Events event)
    {

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_INPUT);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_OUTPUT);

        ParticipantEventDTO dto = new ParticipantEventDTO();
        dto.setEventId(String.valueOf(event.getEventId()));
        dto.setEventName(event.getEventName());
        dto.setEventDescription(event.getEventDescription());
        dto.setEventLocation(event.getEventLocation());
        dto.setEventCapacity(event.getEventCapacity());
        dto.setEventFee(String.valueOf(event.getEventFee()));
        dto.setEventStatus(event.getEventStatus().toString());

        if (event.getEventDate() != null)
        {
            LocalDate date = LocalDate.parse(event.getEventDate(), inputFormatter);
            dto.setEventDate(date.format(outputFormatter));
        }
        return dto;
    }
}
