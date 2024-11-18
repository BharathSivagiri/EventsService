package com.ems.EventsService.services.implementations;

import com.ems.EventsService.model.LoginResponse;
import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.UsersType;
import com.ems.EventsService.exceptions.custom.BasicValidationException;
import com.ems.EventsService.mapper.AuthTokenMapper;
import com.ems.EventsService.mapper.RecordStatusMapper;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.repositories.UsersRepository;
import com.ems.EventsService.utility.constants.ErrorMessages;
import com.ems.EventsService.validations.AuthValidation;

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
public class AuthServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private AuthTokenMapper authTokenMapper;

    public LoginResponse authenticateUser(String customName, String password) {
        Users user = usersRepository.findByCustomNameAndRecStatus(customName, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.USER_NOT_FOUND));

        AuthValidation.validatePassword(password, user.getPassword());
        logger.info("User authenticated successfully");
        return generateToken(user);
    }

    private LoginResponse generateToken(Users user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        AuthToken authToken = authTokenMapper.toEntity(user, token, now);
        authTokenRepository.save(authToken);
        logger.info("Token generated successfully");
        return new LoginResponse(token, user.getUserId());
    }

    public boolean isAdmin(String token) {
        AuthToken authToken = authTokenRepository.findByAuthTokenAndRecStatus(token, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.INVALID_TOKEN));

        Users user = usersRepository.findByUserIdAndRecStatus(authToken.getUserIdAuth(), DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.USER_NOT_FOUND));

        logger.info("User is admin: {}", user.getUserType() == UsersType.ADMIN);
        return user.getUserType() == UsersType.ADMIN;
    }

    @Transactional
    public void validateToken(String token, int userId) {
        AuthToken authToken = authTokenRepository.findByAuthTokenAndRecStatus(token, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BasicValidationException(ErrorMessages.INVALID_TOKEN));

        AuthValidation.validateTokenAccess(authToken, userId);
        AuthValidation.validateAndHandleTokenExpiry(authToken, LocalDateTime.now(), authTokenRepository);
        logger.info("Token validated successfully");
    }

    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void updateExpiredTokens() {
        logger.info("Update expired tokens started");
        LocalDateTime now = LocalDateTime.now();
        List<AuthToken> expiredTokens = authTokenRepository.findByResetTimeBeforeAndRecStatus(now, DBRecordStatus.ACTIVE);

        expiredTokens.forEach(token -> {
            RecordStatusMapper.setInactiveStatus(token);
            authTokenRepository.save(token);
        });

        logger.info("Expired tokens updated successfully");
    }

    public void validateAdminAccess(String token, int userId) {
        validateToken(token, userId);
        AuthValidation.validateAdminPrivilieges(isAdmin(token));
    }
}