//package com.ems.EventsService.services.implementations;
//
//import com.ems.EventsService.model.PaymentRequestDTO;
//import com.ems.EventsService.entity.EmailTemplates;
//import com.ems.EventsService.entity.Events;
//import com.ems.EventsService.entity.EventsRegistration;
//import com.ems.EventsService.entity.Users;
//import com.ems.EventsService.enums.DBRecordStatus;
//import com.ems.EventsService.enums.EventStatus;
//import com.ems.EventsService.exceptions.custom.BusinessValidationException;
//import com.ems.EventsService.mapper.EventsMapper;
//import com.ems.EventsService.mapper.EventsRegistrationMapper;
//import com.ems.EventsService.model.EventsModel;
//import com.ems.EventsService.repositories.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class EventsServiceImplTest {
//
//    @InjectMocks
//    private EventsServiceImpl eventsService;
//
//    @Mock
//    private EventsRepository eventsRepository;
//
//    @Mock
//    private UsersRepository usersRepository;
//
//    @Mock
//    private EventsRegistrationRepository eventsRegistrationRepository;
//
//    @Mock
//    private EmailTemplatesRepository emailTemplatesRepository;
//
//    @Mock
//    private EventsMapper eventsMapper;
//
//    @Mock
//    private EventsRegistrationMapper eventsRegistrationMapper;
//
//    @Mock
//    private EmailServiceImpl emailService;
//
//    @Mock
//    private PaymentClientService paymentClientService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void createEvent_Success() {
//        EventsModel model = new EventsModel();
//        model.setEventName("Test Event");
//        model.setEventDate("20241120");
//
//        Events event = new Events();
//        event.setEventName("Test Event");
//        event.setEventDate("20241120");
//
//        when(eventsMapper.toEntity(any())).thenReturn(event);
//        when(eventsMapper.toModel(any())).thenReturn(model);
//        when(eventsRepository.save(any())).thenReturn(event);
//
//        EventsModel result = eventsService.createEvent(model);
//
//        assertNotNull(result);
//        verify(eventsRepository).save(any());
//    }
//
//    @Test
//    void updateEvent_Success() {
//        Integer eventId = 1;
//        EventsModel model = new EventsModel();
//        model.setEventName("Updated Event");
//        model.setEventDate("20241107");
//
//        Events existingEvent = new Events();
//        existingEvent.setEventId(eventId);
//        existingEvent.setEventName("Old Event");
//        existingEvent.setEventLocation("Old Location");
//        existingEvent.setEventDate("20241107");
//        existingEvent.setEventCapacity(100);
//        existingEvent.setEventFee(50.0);
//
//        when(eventsRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
//        when(eventsRepository.save(any())).thenReturn(existingEvent);
//        when(eventsMapper.toModel(any())).thenReturn(model);
//
//        EventsModel result = eventsService.updateEvent(eventId, model);
//
//        assertNotNull(result);
//        verify(eventsRepository).save(any());
//    }
//
//    @Test
//    void processEventRegistration_Success() {
//        PaymentRequestDTO request = new PaymentRequestDTO();
//        request.setEventId("1");
//        request.setUserId("1");
//
//        Users user = new Users();
//        user.setAccount("ACC123");
//        user.setUsername("Test User");
//        user.setEmail("test@test.com");
//
//        Events event = new Events();
//        event.setEventCapacity(10);
//        event.setEventName("Test Event");
//        event.setEventLocation("Test Location");
//        event.setEventDate("20241107");
//
//        EventsRegistration registration = new EventsRegistration();
//        registration.setId(1);
//
//        EmailTemplates template = new EmailTemplates();
//        template.setTemplateCode("Test template {userName} {eventName} {eventLocation} {eventDate} {registrationId}");
//
//        when(usersRepository.findById(1)).thenReturn(Optional.of(user));
//        when(eventsRepository.findById(1)).thenReturn(Optional.of(event));
//        when(paymentClientService.processPayment(any())).thenReturn(123);
//        when(eventsRegistrationRepository.findByEventIdAndRecordStatus(anyInt(), any()))
//            .thenReturn(List.of());
//        when(eventsRegistrationMapper.toEntity(any(), any(), any(), any())).thenReturn(registration);
//        when(eventsRegistrationRepository.save(any())).thenReturn(registration);
//        when(emailTemplatesRepository.findByTemplateNameAndRecStatus(any(), any()))
//            .thenReturn(Optional.of(template));
//
//        EventsRegistration result = eventsService.processEventRegistration(request);
//
//        assertNotNull(result);
//        verify(paymentClientService).processPayment(any());
//    }
//
//    @Test
//    void getEventParticipants_Success() {
//        Integer eventId = 1;
//        Events event = new Events();
//        event.setEventId(eventId);
//        event.setEventName("Test Event");
//
//        EventsRegistration registration = new EventsRegistration();
//        registration.setUserId(1);
//
//        Users user = new Users();
//        user.setUserId(1);
//        user.setUsername("Test User");
//
//        when(eventsRepository.findById(eventId)).thenReturn(Optional.of(event));
//        when(eventsRegistrationRepository.findByEventIdAndRecordStatus(eventId, DBRecordStatus.ACTIVE))
//            .thenReturn(List.of(registration));
//        when(usersRepository.findById(1)).thenReturn(Optional.of(user));
//
//        List<Map<String, Object>> result = eventsService.getEventParticipants(eventId);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    void cancelEventRegistration_Success() {
//        PaymentRequestDTO request = new PaymentRequestDTO();
//        request.setEventId("1");
//        request.setUserId("1");
//
//        EventsRegistration registration = new EventsRegistration();
//        Events event = new Events();
//        event.setEventCapacity(5);
//
//        when(eventsRegistrationRepository.findByEventIdAndUserIdAndRecordStatus(anyInt(), anyInt(), any()))
//            .thenReturn(Optional.of(registration));
//        when(eventsRepository.findById(anyInt())).thenReturn(Optional.of(event));
//        when(paymentClientService.processRefund(any())).thenReturn(123);
//        when(eventsRegistrationRepository.save(any())).thenReturn(registration);
//
//        EventsRegistration result = eventsService.cancelEventRegistration(request);
//
//        assertNotNull(result);
//        verify(paymentClientService).processRefund(any());
//        verify(eventsRepository).save(any());
//    }
//
//    // Add these test methods to the existing test class
//
//    @Test
//    void createEvent_EmptyName() {
//        EventsModel model = new EventsModel();
//        assertThrows(BusinessValidationException.class, () -> eventsService.createEvent(model));
//    }
//
//    @Test
//    void createEvent_DuplicateName() {
//        EventsModel model = new EventsModel();
//        model.setEventName("Test Event");
//        model.setEventDate("07-11-2024");
//
//        when(eventsRepository.existsByEventNameAndRecStatus(anyString(), any())).thenReturn(true);
//        assertThrows(BusinessValidationException.class, () -> eventsService.createEvent(model));
//    }
//
//    @Test
//    void getAllEvents_WithDateRange() {
//        Events event = createMockEvent();
//        when(eventsRepository.findByEventDateBetweenAndRecStatus(anyString(), anyString(), any()))
//                .thenReturn(List.of(event));
//
//        List<?> results = eventsService.getAllEvents(true, null, "01-11-2024", "30-11-2024");
//        assertFalse(results.isEmpty());
//    }
//
//    @Test
//    void getAllEvents_WithKeyword() {
//        Events event = createMockEvent();
//        when(eventsRepository.findByRecStatusAndEventNameContainingIgnoreCaseOrEventLocationContainingIgnoreCase(
//                any(), anyString(), anyString())).thenReturn(List.of(event));
//
//        List<?> results = eventsService.getAllEvents(true, "Test", null, null);
//        assertFalse(results.isEmpty());
//    }
//    @Test
//    void updateEvent_StatusCancelled() {
//        EventsModel model = new EventsModel();
//        model.setEventStatus(EventStatus.CANCELLED.toString());
//        model.setEventDate("20241107");
//
//        Events existingEvent = createMockEvent();
//        EventsRegistration registration = createMockRegistration();
//        Users user = createMockUser();
//        EmailTemplates template = createMockTemplate("EVENT_CANCELLED");
//
//        when(eventsRepository.findById(1)).thenReturn(Optional.of(existingEvent));
//        when(eventsRepository.save(any())).thenReturn(existingEvent);
//        when(eventsRegistrationRepository.findByEventIdAndRecordStatus(anyInt(), any()))
//            .thenReturn(List.of(registration));
//        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
//        when(emailTemplatesRepository.findByTemplateNameAndRecStatus(anyString(), any()))
//            .thenReturn(Optional.of(template));
//        when(eventsMapper.toModel(any())).thenReturn(model);
//
//        eventsService.updateEvent(1, model);
//
//        verify(emailService).sendHtmlMessage(anyString(), anyString(), anyString());
//    }
//
//    @Test
//    void sendEventReminders() {
//        Events event = createMockEvent();
//        EventsRegistration registration = createMockRegistration();
//        Users user = createMockUser();
//        EmailTemplates template = createMockTemplate("EVENT_NOTIFICATION");
//
//        when(eventsRepository.findByEventDateAndRecStatus(anyString(), any()))
//                .thenReturn(List.of(event));
//        when(eventsRegistrationRepository.findByEventIdAndRecordStatus(anyInt(), any()))
//                .thenReturn(List.of(registration));
//        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(user));
//        when(emailTemplatesRepository.findByTemplateNameAndRecStatus(anyString(), any()))
//                .thenReturn(Optional.of(template));
//
//        eventsService.sendEventReminders();
//
//        verify(emailService).sendHtmlMessage(anyString(), anyString(), anyString());
//    }
//
//    // Helper methods for creating mock objects
//    private Events createMockEvent() {
//        Events event = new Events();
//        event.setEventId(1);
//        event.setEventName("Test Event");
//        event.setEventLocation("Test Location");
//        event.setEventDate("07-11-2024");
//        event.setEventCapacity(100);
//        event.setEventFee(50.0);
//        event.setEventStatus(EventStatus.OPENED);
//        return event;
//    }
//
//    private EventsRegistration createMockRegistration() {
//        EventsRegistration registration = new EventsRegistration();
//        registration.setId(1);
//        registration.setUserId(1);
//        registration.setEventId(1);
//        return registration;
//    }
//
//    private Users createMockUser() {
//        Users user = new Users();
//        user.setUserId(1);
//        user.setUsername("Test User");
//        user.setEmail("test@test.com");
//        user.setAccount("ACC123");
//        return user;
//    }
//
//    private EmailTemplates createMockTemplate(String templateName) {
//        EmailTemplates template = new EmailTemplates();
//        template.setTemplateName(templateName);
//        template.setTemplateCode("Test template {userName} {eventName} {eventLocation} {eventDate} {registrationId}");
//        return template;
//    }
//
//}
