package com.ems.EventsService.validations;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.mapper.RecordStatusMapper;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.utility.constants.ErrorMessages;

import java.time.LocalDateTime;

public class AuthValidation {

    public static void validatePassword(String inputPassword, String storedPassword) {
        if (!inputPassword.equals(storedPassword)) {
            throw new BusinessValidationException(ErrorMessages.INVALID_PASSWORD);
        }
    }

    public static void validateTokenAccess(AuthToken authToken, int userId) {
        if (authToken.getUserIdAuth() != userId) {
            throw new BusinessValidationException(ErrorMessages.USER_MISMATCH);
        }

        if (authToken.getRecStatus() != DBRecordStatus.ACTIVE) {
            throw new BusinessValidationException(ErrorMessages.TOKEN_EXPIRED);
        }
    }

    public static void validateAdminPrivilieges(boolean isAdmin){
        if(!isAdmin){
            throw new BusinessValidationException(ErrorMessages.ACCESS_DENIED);
        }
    }

    public static void validateAndHandleTokenExpiry(AuthToken authToken, LocalDateTime currentTime, AuthTokenRepository authTokenRepository) {
        if (currentTime.isAfter(authToken.getResetTime())) {
            RecordStatusMapper.setInactiveStatus(authToken);
            authTokenRepository.save(authToken);
            throw new BusinessValidationException(ErrorMessages.TOKEN_INACTIVE);
        }
    }

}
