package com.ems.EventsService.controller;

import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.services.AuthService;
import com.ems.EventsService.services.EventsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ems/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Events Management API")public class EventsController
{
    @Autowired
    private EventsService eventsService;

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
    public ResponseEntity<String> updateEvent(
            @RequestHeader("Authorization") String token,
            @Parameter(description = "ID of the event to be updated") @PathVariable Integer eventId,
            @Parameter(description = "Updated event details") @RequestBody EventsModel eventsModel) {
        if (!authService.isAdmin(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        eventsService.updateEvent(eventId, eventsModel);
        return ResponseEntity.status(HttpStatus.OK).body("Event updated successfully");
    }

    @DeleteMapping("/delete/{eventId}")
    public ResponseEntity<String> deleteEvent(@RequestHeader("Authorization") String token, @PathVariable Integer eventId) {
        if (!authService.isAdmin(token.substring(7))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        eventsService.deleteEvent(eventId);
        return ResponseEntity.ok("Event with ID " + eventId + " has been marked as inactive.");
    }

    @GetMapping("/view")
    public ResponseEntity<?> getAllEvents(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) String keyword) {
        boolean isAdmin = authService.isAdmin(token.substring(7));
        List<?> events = eventsService.getAllEvents(isAdmin, keyword != null ? keyword : "");
        return ResponseEntity.ok(events);
    }



}
