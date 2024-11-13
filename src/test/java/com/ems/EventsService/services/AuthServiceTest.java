//package com.ems.EventsService.services;
//
//import com.ems.EventsService.entity.AuthToken;
//import com.ems.EventsService.entity.Users;
//import com.ems.EventsService.enums.DBRecordStatus;
//import com.ems.EventsService.enums.UsersType;
//import com.ems.EventsService.exceptions.custom.BusinessValidationException;
//import com.ems.EventsService.mapper.AuthTokenMapper;
//import com.ems.EventsService.repositories.AuthTokenRepository;
//import com.ems.EventsService.repositories.UsersRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @Mock
//    private UsersRepository usersRepository;
//
//    @Mock
//    private AuthTokenRepository authTokenRepository;
//
//    @Mock
//    private AuthTokenMapper authTokenMapper;
//
//    @InjectMocks
//    private AuthService authService;
//
//    private Users testUser;
//    private AuthToken testAuthToken;
//
//    @BeforeEach
//    void setUp() {
//        testUser = new Users();
//        testUser.setUserId(1);
//        testUser.setCustomName("testUser");
//        testUser.setPassword("password123");
//        testUser.setUserType(UsersType.ADMIN);
//        testUser.setRecStatus(DBRecordStatus.ACTIVE);
//
//        testAuthToken = new AuthToken();
//        testAuthToken.setUserIdAuth(1);
//        testAuthToken.setAuthToken("test-token");
//        testAuthToken.setRecStatus(DBRecordStatus.ACTIVE);
//        testAuthToken.setResetTime(LocalDateTime.now().plusHours(1));
//    }
//
//    @Test
//    void authenticateUser_Success() {
//        when(usersRepository.findByCustomNameAndRecStatus(anyString(), any())).thenReturn(Optional.of(testUser));
//        when(authTokenMapper.toEntity(any(), anyString(), any())).thenReturn(testAuthToken);
//        when(authTokenRepository.save(any())).thenReturn(testAuthToken);
//
//        String token = String.valueOf(authService.authenticateUser("testUser", "password123"));
//        assertNotNull(token);
//        verify(authTokenRepository).save(any());
//    }
//
//    @Test
//    void authenticateUser_InvalidPassword() {
//        when(usersRepository.findByCustomNameAndRecStatus(anyString(), any())).thenReturn(Optional.of(testUser));
//
//        assertThrows(BusinessValidationException.class, () ->
//            authService.authenticateUser("testUser", "wrongPassword")
//        );
//    }
//
//    @Test
//    void isAdmin_Success() {
//        when(authTokenRepository.findByAuthTokenAndRecStatus(anyString(), any())).thenReturn(Optional.of(testAuthToken));
//        when(usersRepository.findById(anyInt())).thenReturn(Optional.of(testUser));
//
//        boolean result = authService.isAdmin("test-token");
//        assertTrue(result);
//    }
//
//    @Test
//    void validateToken_Success() {
//        when(authTokenRepository.findByAuthTokenAndRecStatus(anyString(), any())).thenReturn(Optional.of(testAuthToken));
//
//        authService.validateToken("test-token", 1);
//        verify(authTokenRepository).findByAuthTokenAndRecStatus(anyString(), any());
//    }
//
//    @Test
//    void validateToken_UserMismatch() {
//        when(authTokenRepository.findByAuthTokenAndRecStatus(anyString(), any())).thenReturn(Optional.of(testAuthToken));
//
//        assertThrows(BusinessValidationException.class, () ->
//            authService.validateToken("test-token", 2)
//        );
//    }
//
//    @Test
//    void validateToken_ExpiredToken() {
//        testAuthToken.setResetTime(LocalDateTime.now().minusHours(1));
//        when(authTokenRepository.findByAuthTokenAndRecStatus(anyString(), any())).thenReturn(Optional.of(testAuthToken));
//
//        assertThrows(BusinessValidationException.class, () ->
//            authService.validateToken("test-token", 1)
//        );
//        verify(authTokenRepository).save(any());
//    }
//
//    @Test
//    void updateExpiredTokens_Success() {
//        AuthToken expiredToken = new AuthToken();
//        expiredToken.setRecStatus(DBRecordStatus.ACTIVE);
//
//        when(authTokenRepository.findByResetTimeBeforeAndRecStatus(any(), any()))
//            .thenReturn(Arrays.asList(expiredToken));
//
//        authService.updateExpiredTokens();
//
//        verify(authTokenRepository).findByResetTimeBeforeAndRecStatus(any(), any());
//        verify(authTokenRepository).save(any());
//    }
//}
