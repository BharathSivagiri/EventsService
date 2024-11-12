package com.ems.EventsService.controller;

import com.ems.EventsService.configuration.AuthApiResponses;
import com.ems.EventsService.dto.LoginRequest;
import com.ems.EventsService.dto.LoginResponse;
import com.ems.EventsService.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ems/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Authentication", description = "Authentication Management API")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    @AuthApiResponses.LoginResponses
    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token and userId"
    )
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        // Get both token and userId from auth service
        LoginResponse loginResponse = authService.authenticateUser(loginRequest.getCustomName(), loginRequest.getPassword());
        return ResponseEntity.ok(loginResponse);
    }
}