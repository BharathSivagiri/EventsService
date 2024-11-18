//package com.ems.EventsService.mapper;
//
//import com.ems.EventsService.entity.Events;
//import com.ems.EventsService.enums.DBRecordStatus;
//import com.ems.EventsService.enums.EventStatus;
//import com.ems.EventsService.model.EventsModel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class EventsMapperTest {
//
//    private EventsMapper eventsMapper;
//
//    @BeforeEach
//    void setUp() {
//        eventsMapper = new EventsMapper();
//    }
//
//    @Test
//    void toEntity_Success() {
//        // Arrange
//        EventsModel model = new EventsModel();
//        model.setEventName("Test Event");
//        model.setEventDescription("Test Description");
//        model.setEventDate("20241107");
//        model.setEventLocation("Test Location");
//        model.setEventCapacity(100);
//        model.setEventFee("50.0");
//        model.setEventStatus("OPENED");
//        model.setRecStatus("ACTIVE");
//
//        // Act
//        Events result = eventsMapper.toEntity(model);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("Test Event", result.getEventName());
//        assertEquals("Test Description", result.getEventDescription());
//        assertEquals("2024-11-07", result.getEventDate());
//        assertEquals("Test Location", result.getEventLocation());
//        assertEquals(100, result.getEventCapacity());
//        assertEquals(50.0, result.getEventFee());
//        assertEquals(EventStatus.OPENED, result.getEventStatus());
//        assertEquals(DBRecordStatus.ACTIVE, result.getRecStatus());
//    }
//
//    @Test
//    void toModel_Success() {
//        // Arrange
//        Events entity = new Events();
//        entity.setEventId(1);
//        entity.setEventName("Test Event");
//        entity.setEventDescription("Test Description");
//        entity.setEventDate("2024-11-07");
//        entity.setEventLocation("Test Location");
//        entity.setEventCapacity(100);
//        entity.setEventFee(50.0);
//        entity.setEventStatus(EventStatus.OPENED);
//        entity.setRecStatus(DBRecordStatus.ACTIVE);
//
//        // Act
//        EventsModel result = eventsMapper.toModel(entity);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("1", result.getEventId());
//        assertEquals("Test Event", result.getEventName());
//        assertEquals("Test Description", result.getEventDescription());
//        assertEquals("20241107", result.getEventDate());
//        assertEquals("Test Location", result.getEventLocation());
//        assertEquals(100, result.getEventCapacity());
//        assertEquals("50.0", result.getEventFee());
//        assertEquals("OPENED", result.getEventStatus());
//        assertEquals("ACTIVE", result.getRecStatus());
//    }
//}
