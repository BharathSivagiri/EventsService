package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenMapperTest {

    private AuthTokenMapper authTokenMapper;
    private Users mockUser;
    private String mockToken;
    private LocalDateTime mockDateTime;

    @BeforeEach
    void setUp() {
        authTokenMapper = new AuthTokenMapper();
        mockUser = new Users();
        mockUser.setUserId(1);
        mockUser.setUsername("Test User");
        mockToken = "test-token-123";
        mockDateTime = LocalDateTime.of(2024, 1, 1, 12, 0);
    }

    @Test
    void toEntity_ShouldMapCorrectly() {
        // Act
        AuthToken result = authTokenMapper.toEntity(mockUser, mockToken, mockDateTime);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser.getUserId(), result.getUserIdAuth());
        assertEquals(mockDateTime, result.getCreationTime());
        assertEquals(120, result.getValidFor());
        assertEquals(mockDateTime.plusMinutes(2), result.getResetTime());
        assertEquals(mockToken, result.getAuthToken());
        assertEquals(DBRecordStatus.ACTIVE, result.getRecStatus());
        assertEquals(mockUser.getUsername(), result.getCreatedBy());
        assertEquals(mockDateTime.toString(), result.getCreatedDate());
        assertEquals(mockUser.getUsername(), result.getUpdatedBy());
        assertEquals(mockDateTime.toString(), result.getUpdatedDate());
    }
}
