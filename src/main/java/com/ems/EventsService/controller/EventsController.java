package com.ems.EventsService.controller;

import com.ems.EventsService.dto.ErrorResponse;
import com.ems.EventsService.dto.PaymentRequestDTO;
import com.ems.EventsService.dto.RegistrationResponseDTO;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.services.AuthService;
import com.ems.EventsService.services.EventsService;
import com.ems.EventsService.utility.constants.AppConstants;
import com.ems.EventsService.utility.constants.ErrorMessages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ems/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Events Management API")
public class EventsController {
    @Autowired
    EventsService eventsService;

    private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    AuthService authService;

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Create a new event",
        description = "Creates a new event in the system",
        responses = {
            @ApiResponse(responseCode = "201", description = "Event created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Event already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<String> createEvent(@RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
                                              @RequestHeader(AppConstants.USERID_HEADER) int userId,
                                              @Valid @RequestBody EventsModel eventsModel) {
        authService.validateToken(token, userId);
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.ACCESS_DENIED);
        }

        eventsModel.setCreatedBy(String.valueOf(userId));
        eventsModel.setUpdatedBy(String.valueOf(userId));

        eventsService.createEvent(eventsModel);
        String response = ErrorMessages.EVENT_CREATED;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{eventId}")
    @Operation(
        summary = "Update an event",
        description = "Updates an existing event in the system",
        responses = {
            @ApiResponse(responseCode = "200", description = "Event updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<String> updateEvent(@RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
                                              @RequestHeader(AppConstants.USERID_HEADER) int userId,
                                              @PathVariable Integer eventId,
                                              @RequestBody EventsModel eventsModel) {
        authService.validateToken(token, userId);
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorMessages.ACCESS_DENIED);
        }
        eventsModel.setUpdatedBy(String.valueOf(userId));
        eventsService.updateEvent(eventId, eventsModel);
        return ResponseEntity.status(HttpStatus.OK).body(ErrorMessages.EVENT_UPDATED);
    }

    @DeleteMapping("/delete/{eventId}")
    @Operation(
        summary = "Delete an event",
        description = "Changes the record status from active to inactive",
        responses = {
            @ApiResponse(responseCode = "200", description = "Event deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<String> deleteEvent(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @PathVariable Integer eventId) {
        authService.validateToken(token, userId);
        if (!authService.isAdmin(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        eventsService.deleteEvent(eventId);
        return ResponseEntity.ok(ErrorMessages.EVENT_DELETED);
    }

    @GetMapping("/view")
    @Operation(
            summary = "View all events",
            description = "Retrieves all active events based on user access level. Supports both keyword search and date range filtering",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Events retrieved successfully"),
                    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No events found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> getAllEvents(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String dateA,
            @RequestParam(required = false) String dateB) {
        authService.validateToken(token, userId);
        boolean isAdmin = authService.isAdmin(token);
        List<?> events = eventsService.getAllEvents(isAdmin, keyword != null ? keyword : "", dateA, dateB);
        return ResponseEntity.ok(events);
    }

    @PostMapping("/registration")
    @Operation(
        summary = "Register for an event",
        description = "Registers a user for an event",
        responses = {
            @ApiResponse(responseCode = "201", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Already registered/Event full", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> processRegistration(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestBody PaymentRequestDTO request)  throws BusinessValidationException {
        authService.validateToken(token, userId);
            EventsRegistration registration = eventsService.processEventRegistration(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegistrationResponseDTO(
                            registration.getId(),
                            "SUCCESS",
                            "Registration completed successfully"
                    ));
    }

    @PostMapping("/registration/cancel")
    @Operation(
        summary = "Cancel registration for an event",
        description = "Cancels a user's registration for an event",
        responses = {
            @ApiResponse(responseCode = "200", description = "Registration cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Registration not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<?> cancelRegistration(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestBody PaymentRequestDTO request) {
        authService.validateToken(token, userId);
        EventsRegistration cancelledRegistration = eventsService.cancelEventRegistration(request);
        return ResponseEntity.ok("Registration cancelled successfully and payment refunded");
    }

    @GetMapping("/users/view-participants")
    @Operation(
        summary = "Get event participants",
        description = "Returns list of participants for events",
        responses = {
            @ApiResponse(responseCode = "200", description = "Participants retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Event not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        }
    )
    public ResponseEntity<List<Map<String, Object>>> getEventParticipants(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestParam(required = false) Integer eventId) {
        authService.validateToken(token, userId);
        if (!authService.isAdmin(token)) {
            throw new BusinessValidationException(ErrorMessages.ACCESS_DENIED);
        }
        return ResponseEntity.ok(eventsService.getEventParticipants(eventId));
    }
}


