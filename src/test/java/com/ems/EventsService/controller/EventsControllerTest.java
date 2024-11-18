//package com.ems.EventsService.controller;
//
//import com.ems.EventsService.model.PaymentRequestDTO;
//import com.ems.EventsService.model.RegistrationResponseDTO;
//import com.ems.EventsService.entity.EventsRegistration;
//import com.ems.EventsService.exceptions.custom.BusinessValidationException;
//import com.ems.EventsService.model.EventsModel;
//import com.ems.EventsService.services.implementations.AuthService;
//import com.ems.EventsService.services.EventsService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class EventsControllerTest {
//
//    @Mock
//    private EventsService eventsService;
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private EventsController eventsController;
//
//    private static final String TOKEN = "test-token";
//    private static final int USER_ID = 1;
//
//    @Test
//    void createEventSuccess() {
//        EventsModel model = new EventsModel();
//        // Update the mock to use validateAdminAccess instead of isAdmin
//        doNothing().when(authService).validateAdminAccess(TOKEN, USER_ID);
//
//        ResponseEntity<String> response = eventsController.createEvent(TOKEN, USER_ID, model);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        verify(eventsService).createEvent(model);
//    }
//
//    @Test
//    void createEventAccessDenied() {
//        EventsModel model = new EventsModel();
//        doThrow(new BusinessValidationException("Access Denied"))
//            .when(authService).validateAdminAccess(TOKEN, USER_ID);
//
//        assertThrows(BusinessValidationException.class, () ->
//            eventsController.createEvent(TOKEN, USER_ID, model));
//    }
//
//    @Test
//    void updateEventSuccess() {
//        EventsModel model = new EventsModel();
//        doNothing().when(authService).validateAdminAccess(TOKEN, USER_ID);
//
//        ResponseEntity<String> response = eventsController.updateEvent(TOKEN, USER_ID, 1, model);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(eventsService).updateEvent(1, model);
//    }
//
//    @Test
//    void deleteEventSuccess() {
//        doNothing().when(authService).validateAdminAccess(TOKEN, USER_ID);
//
//        ResponseEntity<String> response = eventsController.deleteEvent(TOKEN, USER_ID, 1);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(eventsService).deleteEvent(1);
//    }
//
//    @Test
//    void processRegistrationSuccess() {
//        PaymentRequestDTO request = new PaymentRequestDTO();
//        EventsRegistration registration = new EventsRegistration();
//        registration.setId(1);
//
//        doNothing().when(authService).validateToken(TOKEN, USER_ID);
//        when(eventsService.processEventRegistration(request)).thenReturn(registration);
//
//        ResponseEntity<?> response = eventsController.processRegistration(TOKEN, USER_ID, request);
//
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertTrue(response.getBody() instanceof RegistrationResponseDTO);
//    }
//
//    @Test
//    void cancelRegistrationSuccess() {
//        PaymentRequestDTO request = new PaymentRequestDTO();
//        EventsRegistration registration = new EventsRegistration();
//
//        doNothing().when(authService).validateToken(TOKEN, USER_ID);
//        when(eventsService.cancelEventRegistration(request)).thenReturn(registration);
//
//        ResponseEntity<?> response = eventsController.cancelRegistration(TOKEN, USER_ID, request);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    void getEventParticipantsSuccess() {
//        List<Map<String, Object>> participants = List.of(Map.of("userId", 1));
//        doNothing().when(authService).validateAdminAccess(TOKEN, USER_ID);
//        when(eventsService.getEventParticipants(1)).thenReturn(participants);
//
//        ResponseEntity<List<Map<String, Object>>> response =
//            eventsController.getEventParticipants(TOKEN, USER_ID, 1);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(participants, response.getBody());
//    }
//
//    @Test
//    void getEventParticipantsAccessDenied() {
//        doThrow(new BusinessValidationException("Access Denied"))
//                .when(authService).validateAdminAccess(TOKEN, USER_ID);
//
//        assertThrows(BusinessValidationException.class, () ->
//                eventsController.getEventParticipants(TOKEN, USER_ID, 1));
//    }
//}