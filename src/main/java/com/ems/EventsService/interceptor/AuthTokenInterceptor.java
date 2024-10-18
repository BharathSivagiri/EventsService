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
        if (token == null || !token.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        try {
            token = token.substring(7);
            authService.validateToken(token);
        } catch (BusinessValidationException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return false;
        }


        return true;
    }
}

