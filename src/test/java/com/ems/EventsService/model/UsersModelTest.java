package com.ems.EventsService.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsersModelTest {

    @Test
    void testUsersModelGettersAndSetters() {
        // Arrange
        UsersModel usersModel = new UsersModel();

        // Act
        usersModel.setUserId("USER123");
        usersModel.setUsername("JohnDoe");
        usersModel.setCustomName("John");
        usersModel.setPassword("pass123");
        usersModel.setEmail("john@example.com");
        usersModel.setMobile("1234567890");
        usersModel.setAddress("123 Main St");
        usersModel.setAccount("ACC001");
        usersModel.setUserType("ADMIN");
        usersModel.setRecStatus("ACTIVE");
        usersModel.setCreatedBy("System");
        usersModel.setCreatedDate("2024-01-07");
        usersModel.setUpdatedBy("Admin");
        usersModel.setUpdatedDate("2024-01-07");

        // Assert
        assertEquals("USER123", usersModel.getUserId());
        assertEquals("JohnDoe", usersModel.getUsername());
        assertEquals("John", usersModel.getCustomName());
        assertEquals("pass123", usersModel.getPassword());
        assertEquals("john@example.com", usersModel.getEmail());
        assertEquals("1234567890", usersModel.getMobile());
        assertEquals("123 Main St", usersModel.getAddress());
        assertEquals("ACC001", usersModel.getAccount());
        assertEquals("ADMIN", usersModel.getUserType());
        assertEquals("ACTIVE", usersModel.getRecStatus());
        assertEquals("System", usersModel.getCreatedBy());
        assertEquals("2024-01-07", usersModel.getCreatedDate());
        assertEquals("Admin", usersModel.getUpdatedBy());
        assertEquals("2024-01-07", usersModel.getUpdatedDate());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UsersModel user1 = new UsersModel();
        UsersModel user2 = new UsersModel();

        user1.setUserId("USER123");
        user2.setUserId("USER123");

        // Assert
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UsersModel usersModel = new UsersModel();
        usersModel.setUserId("USER123");
        usersModel.setUsername("JohnDoe");

        // Act
        String toString = usersModel.toString();

        // Assert
        assertTrue(toString.contains("USER123"));
        assertTrue(toString.contains("JohnDoe"));
    }
}
