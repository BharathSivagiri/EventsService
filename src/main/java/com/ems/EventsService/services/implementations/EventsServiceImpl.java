package com.ems.EventsService.services.implementations;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.exceptions.custom.BusinessValidationException;
import com.ems.EventsService.mapper.EventsMapper;
import com.ems.EventsService.model.EventsModel;
import com.ems.EventsService.repositories.EventsRepository;
import com.ems.EventsService.services.EventsService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EventsServiceImpl implements EventsService
{
    private static final Logger logger = LoggerFactory.getLogger(EventsServiceImpl.class);

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private EventsMapper eventsMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine tempEngine;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EventsModel createEvent(EventsModel eventsModel)
    {
        logger.info("Event creation for " + eventsModel.getEventName() + " started");
        if (eventsModel.getEventName() == null || eventsModel.getEventName().trim().isEmpty()) {
            throw new BusinessValidationException("Event name cannot be empty");
        }
        if (eventsRepository.existsByEventName(eventsModel.getEventName())) {
            throw new BusinessValidationException("An event with the name '" + eventsModel.getEventName() + "' already exists");
        }
        if (eventsModel.getEventDate() == null) {
            throw new BusinessValidationException("Event date cannot be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate eventDate = LocalDate.parse(eventsModel.getEventDate(), formatter);
        if (eventDate.isBefore(LocalDate.now())) {
            throw new BusinessValidationException("Event date cannot be in the past");
        }
        Events events = eventsMapper.toEntity(eventsModel);
        events.setCreatedBy("Admin");
        events.setUpdatedBy("Admin");
        events.setCreatedDate(String.valueOf(LocalDate.now()));
        events.setUpdatedDate(String.valueOf(LocalDate.now()));
        Events savedEvent = eventsRepository.save(events);
        logger.info("Event creation for " + eventsModel.getEventName() + " completed");
        return eventsMapper.toModel(savedEvent);
    }
}
