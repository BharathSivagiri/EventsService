package com.ems.EventsService.validations;

import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.exceptions.custom.BasicValidationException;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRegistrationRepository;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.utility.constants.AppConstants;
import com.ems.EventsService.utility.constants.ErrorMessages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventValidation {
    private final EventsRepository eventsRepository;
    private final EventsRegistrationRepository eventsRegistrationRepository;

    public void validateEventCreation(EventsModel eventsModel) {
        if (eventsModel.getEventName() == null || eventsModel.getEventName().trim().isEmpty()) {
            throw new BasicValidationException(ErrorMessages.EVENT_NAME_EMPTY);
        }
        if (eventsRepository.existsByEventNameAndRecStatus(eventsModel.getEventName(), DBRecordStatus.ACTIVE)) {
            throw new BusinessValidationException(ErrorMessages.EVENT_NAME_EXISTS);
        }
        validateEventDate(eventsModel.getEventDate());
    }

    public void validateEventDate(String date) {
        if (date == null) {
            throw new BasicValidationException(ErrorMessages.EVENT_DATE_NULL);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);
        LocalDate eventDate = LocalDate.parse(date, formatter);
        if (eventDate.isBefore(LocalDate.now())) {
            throw new BusinessValidationException(ErrorMessages.EVENT_DATE_PAST);
        }
    }

    public void validateRegistration(String eventId, String userId) {
        boolean isAlreadyRegistered = eventsRegistrationRepository
                .findByEventIdAndRecordStatus(Integer.parseInt(eventId), DBRecordStatus.ACTIVE)
                .stream()
                .anyMatch(reg -> reg.getUserId().equals(Integer.parseInt(userId)));

        if (isAlreadyRegistered) {
            throw new BusinessValidationException(ErrorMessages.EVENT_ALREADY_REGISTERED);
        }
    }
}
