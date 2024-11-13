//package com.ems.EventsService.mapper;
//
//import com.ems.EventsService.dto.ParticipantEventDTO;
//import com.ems.EventsService.entity.Events;
//import com.ems.EventsService.enums.EventStatus;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ParticipantEventDTOMapperTest {
//
//    private ParticipantEventDTOMapper mapper;
//    private Events mockEvent;
//
//    @BeforeEach
//    void setUp() {
//        mapper = new ParticipantEventDTOMapper();
//        mockEvent = new Events();
//        mockEvent.setEventId(1);
//        mockEvent.setEventName("Test Event");
//        mockEvent.setEventDescription("Test Description");
//        mockEvent.setEventLocation("Test Location");
//        mockEvent.setEventCapacity(100);
//        mockEvent.setEventFee(50.0);
//        mockEvent.setEventStatus(EventStatus.OPENED);
//        mockEvent.setEventDate("2024-01-07"); // Changed from "20240107" to include hyphens
//    }
//
//    @Test
//    void toDTO_WithValidEvent_ShouldMapAllFields() {
//        ParticipantEventDTO result = mapper.toDTO(mockEvent);
//
//        assertNotNull(result);
//        assertEquals("1", result.getEventId());
//        assertEquals("Test Event", result.getEventName());
//        assertEquals("Test Description", result.getEventDescription());
//        assertEquals("Test Location", result.getEventLocation());
//        assertEquals(100, result.getEventCapacity());
//        assertEquals("50.0", result.getEventFee());
//        assertEquals("OPENED", result.getEventStatus());
//        assertEquals("07-01-2024", result.getEventDate());
//    }
//
//    @Test
//    void toDTO_WithNullEventDate_ShouldMapOtherFields() {
//        mockEvent.setEventDate(null);
//
//        ParticipantEventDTO result = mapper.toDTO(mockEvent);
//
//        assertNotNull(result);
//        assertEquals("1", result.getEventId());
//        assertEquals("Test Event", result.getEventName());
//        assertEquals("Test Description", result.getEventDescription());
//        assertEquals("Test Location", result.getEventLocation());
//        assertEquals(100, result.getEventCapacity());
//        assertEquals("50.0", result.getEventFee());
//        assertEquals("OPENED", result.getEventStatus());
//        assertNull(result.getEventDate());
//    }
//
//    @Test
//    void toDTO_WithNullEvent_ShouldHandleNullPointer() {
//        assertThrows(NullPointerException.class, () -> mapper.toDTO(null));
//    }
//}
