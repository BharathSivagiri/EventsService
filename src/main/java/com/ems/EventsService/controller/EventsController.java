package com.ems.EventsService.controller;

import com.ems.EventsService.configuration.EventsApiResponses;
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
    @EventsApiResponses.CreateEventResponses
    @Operation(
        summary = "Create a new event",
        description = "Creates a new event in the system"
    )
    public ResponseEntity<String> createEvent(@RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
                                              @RequestHeader(AppConstants.USERID_HEADER) int userId,
                                              @Valid @RequestBody EventsModel eventsModel) {
        authService.validateAdminAccess(token, userId);
        eventsModel.setCreatedBy(String.valueOf(userId));
        eventsModel.setUpdatedBy(String.valueOf(userId));

        eventsService.createEvent(eventsModel);
        String response = ErrorMessages.EVENT_CREATED;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{eventId}")
    @EventsApiResponses.UpdateEventResponses
    @Operation(
        summary = "Update an event",
        description = "Updates an existing event in the system"
    )
    public ResponseEntity<String> updateEvent(@RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
                                              @RequestHeader(AppConstants.USERID_HEADER) int userId,
                                              @PathVariable Integer eventId,
                                              @RequestBody EventsModel eventsModel) {
        authService.validateAdminAccess(token, userId);
        eventsModel.setUpdatedBy(String.valueOf(userId));
        eventsService.updateEvent(eventId, eventsModel);
        return ResponseEntity.status(HttpStatus.OK).body(ErrorMessages.EVENT_UPDATED);
    }

    @DeleteMapping("/delete/{eventId}")
    @EventsApiResponses.DeleteEventResponses
    @Operation(
        summary = "Delete an event",
        description = "Changes the record status from active to inactive"
    )
    public ResponseEntity<String> deleteEvent(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @PathVariable Integer eventId) {
        authService.validateAdminAccess(token, userId);
        eventsService.deleteEvent(eventId);
        return ResponseEntity.ok(ErrorMessages.EVENT_DELETED);
    }

    @GetMapping("/view")
    @EventsApiResponses.GetAllEventsResponses
    @Operation(
            summary = "View all events",
            description = "Retrieves all active events based on user access level. Supports both keyword search and date range filtering"
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
    @EventsApiResponses.ProcessRegistrationResponses
    @Operation(
        summary = "Register for an event",
        description = "Registers a user for an event"
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
    @EventsApiResponses.CancelRegistrationResponses
    @Operation(
        summary = "Cancel registration for an event",
        description = "Cancels a user's registration for an event"
    )
    public ResponseEntity<?> cancelRegistration(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestBody PaymentRequestDTO request) {
        authService.validateToken(token, userId);
        request.setUserId(String.valueOf(userId)); // Ensure userId from header is used
        EventsRegistration cancelledRegistration = eventsService.cancelEventRegistration(request);
        return ResponseEntity.ok("Registration cancelled successfully and payment refunded");
    }

    @GetMapping("/users/view-participants")
    @EventsApiResponses.GetEventParticipantsResponses
    @Operation(
        summary = "Get event participants",
        description = "Returns list of participants for events"
    )
    public ResponseEntity<List<Map<String, Object>>> getEventParticipants(
            @RequestHeader(AppConstants.AUTHORIZATION_HEADER) String token,
            @RequestHeader(AppConstants.USERID_HEADER) int userId,
            @RequestParam(required = false) Integer eventId) {
        authService.validateAdminAccess(token, userId);
        return ResponseEntity.ok(eventsService.getEventParticipants(eventId));
    }
}


