//package com.ems.EventsService.model;
//
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class EventsModelTest {
//    private Validator validator;
//    private EventsModel eventsModel;
//
//    @BeforeEach
//    void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//        eventsModel = new EventsModel();
//    }
//
//    @Test
//    void validEventModel() {
//        eventsModel.setEventName("Tech Conference 2024");
//        eventsModel.setEventDescription("Annual Technology Conference");
//        eventsModel.setEventDate("20240515");
//        eventsModel.setEventLocation("Convention Center");
//        eventsModel.setEventCapacity(500);
//        eventsModel.setEventFee("99.99");
//        eventsModel.setEventStatus("opened");
//
//        var violations = validator.validate(eventsModel);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventName() {
//        eventsModel.setEventName("Tech@Conference#2024");
//        var violations = validator.validate(eventsModel);
//        assertFalse(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventDate() {
//        eventsModel.setEventDate("2024-05-15");
//        var violations = validator.validate(eventsModel);
//        assertFalse(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventLocation() {
//        eventsModel.setEventLocation("Convention@Center123");
//        var violations = validator.validate(eventsModel);
//        assertFalse(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventCapacity() {
//        eventsModel.setEventCapacity(1000001);
//        var violations = validator.validate(eventsModel);
//        assertFalse(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventFee() {
//        eventsModel.setEventFee("99.999");
//        var violations = validator.validate(eventsModel);
//        assertFalse(violations.isEmpty());
//    }
//
//    @Test
//    void invalidEventStatus() {
//        // Setup complete model with valid values first
//        eventsModel.setEventName("Tech Conference 2024");
//        eventsModel.setEventDate("20240515");
//        eventsModel.setEventLocation("Convention Center");
//        eventsModel.setEventCapacity(500);
//        eventsModel.setEventFee("99.99");
//        // Set invalid status
//        eventsModel.setEventStatus("pending");
//
//        var violations = validator.validate(eventsModel);
//        assertTrue(!violations.isEmpty());
//    }
//
//
//    @Test
//    void testGettersAndSetters() {
//        String eventId = "E123";
//        String eventName = "Tech Summit";
//        String description = "Annual Summit";
//        String date = "20240601";
//        String location = "Conference Hall";
//        Integer capacity = 300;
//        String fee = "149.99";
//        String status = "opened";
//
//        eventsModel.setEventId(eventId);
//        eventsModel.setEventName(eventName);
//        eventsModel.setEventDescription(description);
//        eventsModel.setEventDate(date);
//        eventsModel.setEventLocation(location);
//        eventsModel.setEventCapacity(capacity);
//        eventsModel.setEventFee(fee);
//        eventsModel.setEventStatus(status);
//
//        assertEquals(eventId, eventsModel.getEventId());
//        assertEquals(eventName, eventsModel.getEventName());
//        assertEquals(description, eventsModel.getEventDescription());
//        assertEquals(date, eventsModel.getEventDate());
//        assertEquals(location, eventsModel.getEventLocation());
//        assertEquals(capacity, eventsModel.getEventCapacity());
//        assertEquals(fee, eventsModel.getEventFee());
//        assertEquals(status, eventsModel.getEventStatus());
//    }
//}
