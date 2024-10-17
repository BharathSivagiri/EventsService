package com.ems.EventsService.services;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.repositories.AuthTokenRepository;
import com.ems.EventsService.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AuthTokenRepository authTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUser(String username, String password) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return generateToken(user);
    }


    private String generateToken(Users user) {
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        AuthToken authToken = new AuthToken();
        authToken.setUserIdAuth(user.getUserId());
        authToken.setCreationTime(now);
        authToken.setValidFor(120); // 2 minutes
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

    public boolean validateToken(String token) {
        AuthToken authToken = authTokenRepository.findByAuthToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (authToken.getResetTime().isBefore(LocalDateTime.now())) {
            authToken.setRecStatus(DBRecordStatus.INACTIVE);
            authTokenRepository.save(authToken);
            return false;
        }

        return true;
    }
}

