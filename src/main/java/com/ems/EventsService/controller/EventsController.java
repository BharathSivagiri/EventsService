package com.ems.EventsService.controller;

import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.services.EventsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ems/events")
@RequiredArgsConstructor
public class EventsController
{
    @Autowired
    private EventsService eventsService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createEvent(@Valid @RequestBody EventsModel eventsModel) {
        EventsModel createdEvent = eventsService.createEvent(eventsModel);
        String response = "Event successfully created";
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
