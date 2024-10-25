package com.ems.EventsService.services;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.UsersType;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.mapper.AuthTokenMapper;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.repositories.UsersRepository;
import com.ems.EventsService.utility.constants.ErrorMessages;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    AuthTokenRepository authTokenRepository;

    @Autowired
    AuthTokenMapper authTokenMapper;

    public String authenticateUser(String customName, String password) {
        Users user = usersRepository.findByCustomNameAndRecStatus(customName, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.USER_NOT_FOUND));

        if (!password.equals(user.getPassword())) {
            throw new BusinessValidationException(ErrorMessages.INVALID_PASSWORD);
        }
        logger.info("User authenticated successfully");
        return generateToken(user);
    }

    private String generateToken(Users user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        AuthToken authToken = authTokenMapper.toEntity(user, token, now);
        authTokenRepository.save(authToken);
        logger.info("Token generated successfully");
        return token;
    }

    public boolean isAdmin(String token) {
        AuthToken authToken = authTokenRepository.findByAuthTokenAndRecStatus(token, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.INVALID_TOKEN));

        Users user = usersRepository.findById(authToken.getUserIdAuth())
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.USER_NOT_FOUND));
        logger.info("User is admin: {}", user.getUserType() == UsersType.ADMIN);
        return user.getUserType() == UsersType.ADMIN;
    }

    @Transactional
    public void validateToken(String token, int userId) {
        AuthToken authToken = authTokenRepository.findByAuthTokenAndRecStatus(token, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException(ErrorMessages.INVALID_TOKEN));

        if (authToken.getUserIdAuth() != userId) {
            throw new BusinessValidationException(ErrorMessages.USER_MISMATCH);
        }

        if (authToken.getRecStatus() != DBRecordStatus.ACTIVE) {
            throw new BusinessValidationException(ErrorMessages.TOKEN_EXPIRED);
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(authToken.getResetTime())) {
            authToken.setRecStatus(DBRecordStatus.INACTIVE);
            authTokenRepository.save(authToken);
            throw new BusinessValidationException(ErrorMessages.TOKEN_INACTIVE);
        }
        logger.info("Token validated successfully");
    }

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void updateExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<AuthToken> expiredTokens = authTokenRepository.findByResetTimeBeforeAndRecStatus(now, DBRecordStatus.ACTIVE);
        for (AuthToken token : expiredTokens) {
            token.setRecStatus(DBRecordStatus.INACTIVE);
            authTokenRepository.save(token);
        }
        logger.info("Expired tokens updated successfully");
    }
}
