package com.ems.EventsService.validations;

import com.ems.EventsService.dto.PaymentRequestDTO;
import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.exceptions.custom.BasicValidationException;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.exceptions.custom.DataNotFoundException;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRegistrationRepository;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.repositories.UsersRepository;
import com.ems.EventsService.utility.constants.AppConstants;
import com.ems.EventsService.utility.constants.ErrorMessages;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventValidation {
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
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

    public void validateEventCapacity(Events event){
        if (event.getEventCapacity() <= 0) {
            throw new BusinessValidationException(ErrorMessages.EVENT_FULL_CAPACITY);
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

    public Events getEventById(Integer eventId) {
        return eventsRepository.findByEventIdAndRecStatus(eventId, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.EVENT_NOT_FOUND));
    }

    public Users getUserById(Integer userId) {
        return usersRepository.findByUserIdAndRecStatus(userId, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.USER_NOT_FOUND));
    }

    public EventsRegistration getRegistration(PaymentRequestDTO request) {
        return eventsRegistrationRepository
                .findByEventIdAndUserIdAndRecordStatus(
                        Integer.parseInt(request.getEventId()),
                        Integer.parseInt(request.getUserId()),
                        DBRecordStatus.ACTIVE
                ).orElseThrow(() -> new BasicValidationException(ErrorMessages.REGISTRATION_NOT_FOUND));
    }

    public void validateEventResults(List<?> results) {
        if (results.isEmpty()) {
            throw new DataNotFoundException(ErrorMessages.NO_EVENTS_FOUND);
        }
    }

}
