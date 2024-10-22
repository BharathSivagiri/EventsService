package com.ems.EventsService.services;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.UsersType;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.mapper.AuthTokenMapper;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.repositories.UsersRepository;

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
    private UsersRepository usersRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private AuthTokenMapper authTokenMapper;

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

        AuthToken authToken = authTokenMapper.toEntity(user, token, now);
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

    @Transactional
    public void validateTokenWithUserId(String token, int userId) {
        AuthToken authToken = authTokenRepository.findByAuthTokenAndRecStatus(token, DBRecordStatus.ACTIVE)
                .orElseThrow(() -> new BusinessValidationException("Invalid or inactive token"));

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
    @Transactional
    public void updateExpiredTokens() {
//        logger.info("Updating expired tokens");
        LocalDateTime now = LocalDateTime.now();
        List<AuthToken> expiredTokens = authTokenRepository.findByResetTimeBeforeAndRecStatus(now, DBRecordStatus.ACTIVE);
//        logger.info("Found {} expired tokens", expiredTokens.size());
        for (AuthToken token : expiredTokens) {
            token.setRecStatus(DBRecordStatus.INACTIVE);
            authTokenRepository.save(token);
        }
//        logger.info("Finished updating expired tokens");
    }
}
