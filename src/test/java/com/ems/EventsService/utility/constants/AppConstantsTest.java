package com.ems.EventsService.utility.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppConstantsTest {

    @Test
    void verifyConstantValues() {
        assertEquals("yyyyMMdd", AppConstants.DATE_FORMAT);
        assertEquals("Admin", AppConstants.ADMIN_ROLE);
        assertEquals("yyyy-MM-dd", AppConstants.DATE_INPUT);
        assertEquals("dd-MM-yyyy", AppConstants.DATE_OUTPUT);
        assertEquals("Authorization", AppConstants.AUTHORIZATION_HEADER);
        assertEquals("userId", AppConstants.USERID_HEADER);
    }

    @Test
    void verifyConstantsAreNotNull() {
        assertNotNull(AppConstants.DATE_FORMAT);
        assertNotNull(AppConstants.ADMIN_ROLE);
        assertNotNull(AppConstants.DATE_INPUT);
        assertNotNull(AppConstants.DATE_OUTPUT);
        assertNotNull(AppConstants.AUTHORIZATION_HEADER);
        assertNotNull(AppConstants.USERID_HEADER);
    }
}
