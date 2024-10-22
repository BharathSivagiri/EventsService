package com.ems.EventsService.interceptor;

import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String userIdHeader = request.getHeader("userId");

        if (token == null || token.isEmpty()) {
            throw new BusinessValidationException("No token provided");
        }

        if (userIdHeader == null || userIdHeader.isEmpty()) {
            throw new BusinessValidationException("No userId provided");
        }

        int userId;
        try {
            userId = Integer.parseInt(userIdHeader);
        } catch (NumberFormatException e) {
            throw new BusinessValidationException("Invalid userId format");
        }

        authService.validateTokenWithUserId(token, userId);

        return true;
    }
}


