//package com.ems.EventsService.dto;
//
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//
//class ParticipantEventDTOTest {
//
//    @Test
//    void testParticipantEventDTO() {
//        // Create instance
//        ParticipantEventDTO dto = new ParticipantEventDTO();
//
//        // Set test values
//        dto.setEventId("1");
//        dto.setEventName("Tech Conference");
//        dto.setEventDescription("Annual Technology Conference");
//        dto.setEventDate("20241107");
//        dto.setEventLocation("Convention Center");
//        dto.setEventCapacity(500);
//        dto.setEventFee("199.99");
//        dto.setEventStatus("ACTIVE");
//
//        // Assert all fields
//        assertEquals("1", dto.getEventId());
//        assertEquals("Tech Conference", dto.getEventName());
//        assertEquals("Annual Technology Conference", dto.getEventDescription());
//        assertEquals("20241107", dto.getEventDate());
//        assertEquals("Convention Center", dto.getEventLocation());
//        assertEquals(500, dto.getEventCapacity());
//        assertEquals("199.99", dto.getEventFee());
//        assertEquals("ACTIVE", dto.getEventStatus());
//    }
//
//    @Test
//    void testEqualsAndHashCode() {
//        ParticipantEventDTO dto1 = new ParticipantEventDTO();
//        ParticipantEventDTO dto2 = new ParticipantEventDTO();
//
//        dto1.setEventId("1");
//        dto1.setEventName("Tech Conference");
//
//        dto2.setEventId("1");
//        dto2.setEventName("Tech Conference");
//
//        // Test equals and hashCode
//        assertEquals(dto1, dto2);
//        assertEquals(dto1.hashCode(), dto2.hashCode());
//    }
//
//    @Test
//    void testToString() {
//        ParticipantEventDTO dto = new ParticipantEventDTO();
//        dto.setEventId("1");
//        dto.setEventName("Tech Conference");
//
//        String toString = dto.toString();
//
//        assertTrue(toString.contains("eventId=1"));
//        assertTrue(toString.contains("eventName=Tech Conference"));
//    }
//}
