package com.ems.EventsService.services;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.UsersType;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.repositories.UsersRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    public String authenticateUser(String username, String password) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessValidationException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new BusinessValidationException("Invalid password");
        }

        return generateToken(user);
    }

    private String generateToken(Users user) {
        List<AuthToken> existingTokens = authTokenRepository.findByUserIdAuthAndRecStatus(user.getUserId(), DBRecordStatus.ACTIVE);
        for (AuthToken token : existingTokens) {
            token.setRecStatus(DBRecordStatus.INACTIVE);
        }
        authTokenRepository.saveAll(existingTokens);

        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

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

        authTokenRepository.save(authToken);

        return token;
    }

    public boolean isAdmin(String token) {
        AuthToken authToken = authTokenRepository.findByAuthToken(token)
                .orElseThrow(() -> new BusinessValidationException("Invalid token"));

        Users user = usersRepository.findById(authToken.getUserIdAuth())
                .orElseThrow(() -> new BusinessValidationException("User not found"));

        return user.getUserType() == UsersType.ADMIN;
    }

    public void validateTokenWithUserId(String token, int userId) {
        AuthToken authToken = authTokenRepository.findByAuthToken(token)
                .orElseThrow(() -> new BusinessValidationException("Invalid token"));

        if (authToken.getRecStatus() == DBRecordStatus.INACTIVE) {
            throw new BusinessValidationException("Token is inactive and cannot be used");
        }

        if (authToken.getUserIdAuth() != userId) {
            throw new BusinessValidationException("UserId mismatch");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(authToken.getResetTime())) {
            authToken.setRecStatus(DBRecordStatus.INACTIVE);
            authTokenRepository.save(authToken);
            throw new BusinessValidationException("Token has expired");
        }
    }

    @Scheduled(fixedRate = 120000)
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<AuthToken> expiredTokens = authTokenRepository.findByResetTimeBeforeAndRecStatus(now, DBRecordStatus.ACTIVE);
        for (AuthToken token : expiredTokens) {
            token.setRecStatus(DBRecordStatus.INACTIVE);
        }
        authTokenRepository.saveAll(expiredTokens);
    }
}
