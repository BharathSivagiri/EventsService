package com.ems.EventsService.controller;

import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.services.AuthService;
import com.ems.EventsService.services.EventsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ems/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Events Management API")
public class EventsController
{
    @Autowired
    private EventsService eventsService;

    private static final Logger logger = LoggerFactory.getLogger(EventsController.class);


    @Autowired
    private AuthService authService;
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new event", description = "Creates a new event in the system")
    public ResponseEntity<String> createEvent(@RequestHeader("Authorization") String token, @Valid @RequestBody EventsModel eventsModel) {
        if (!authService.isAdmin(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        int userId = authService.getUserIdFromToken(token.substring(7));

        eventsModel.setCreatedBy(String.valueOf(userId));
        eventsModel.setUpdatedBy(String.valueOf(userId));

        EventsModel createdEvent = eventsService.createEvent(eventsModel);
        String response = "Event successfully created";
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/update/{eventId}")
    @Operation(summary = "Updating a event", description = "Updates a existing event in the system")
    public ResponseEntity<String> updateEvent(
            @RequestHeader("Authorization") String token, @PathVariable Integer eventId, @RequestBody EventsModel eventsModel) {
        if (!authService.isAdmin(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        eventsService.updateEvent(eventId, eventsModel);
        return ResponseEntity.status(HttpStatus.OK).body("Event updated successfully");
    }

    @DeleteMapping("/delete/{eventId}")
    @Operation(summary = "Deleting the event", description = "Changes the record status from active to inactive")
    public ResponseEntity<String> deleteEvent(@RequestHeader("Authorization") String token, @PathVariable Integer eventId) {
        if (!authService.isAdmin(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        eventsService.deleteEvent(eventId);
        return ResponseEntity.ok("Event with ID " + eventId + " has been marked as inactive.");
    }

    @GetMapping("/view")
    @Operation(summary = "View all events", description = "Retrieves all active events based on user access level")
    public ResponseEntity<?> getAllEvents(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String keyword) {
        boolean isAdmin = authService.isAdmin(token.substring(7));
        List<?> events = eventsService.getAllEvents(isAdmin, keyword != null ? keyword : "");
        return ResponseEntity.ok(events);
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registerForEvent(@RequestBody Map<String, String> registrationData) {
        String transactionId = registrationData.get("transactionId");
        String eventId = registrationData.get("eventId");
        String createdBy = registrationData.get("createdBy");

        logger.info("Received registration request - transactionId: {}, eventId: {}, createdBy: {}",
                transactionId, eventId, createdBy);

        if (transactionId == null || transactionId.isEmpty()) {
            logger.error("Transaction ID is null or empty");
            return ResponseEntity.badRequest().body("Transaction ID is required");
        }

        EventsRegistration registration = eventsService.registerForEvent(transactionId, eventId, createdBy);

        logger.info("Registration completed - registrationId: {}, transactionId: {}",
                registration.getId(), registration.getTransactionId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully registered for event with ID: " + eventId +
                        ". Registration ID: " + registration.getId());
    }
}


