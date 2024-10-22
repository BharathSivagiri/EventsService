package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AuthTokenMapper {
    public AuthToken toEntity(Users user, String token, LocalDateTime now) {
        AuthToken authToken = new AuthToken();
        authToken.setUserIdAuth(user.getUserId());
        authToken.setCreationTime(now);
        authToken.setValidFor(120); //120 Seconds = 2 Minutes
        authToken.setResetTime(now.plusMinutes(2));
        authToken.setAuthToken(token);
        authToken.setRecStatus(DBRecordStatus.ACTIVE);
        authToken.setCreatedBy(user.getUsername());
        authToken.setCreatedDate(now.toString());
        authToken.setUpdatedBy(user.getUsername());
        authToken.setUpdatedDate(now.toString());
        return authToken;
    }
}

