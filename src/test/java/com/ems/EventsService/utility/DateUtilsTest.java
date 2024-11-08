package com.ems.EventsService.utility;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void convertToDate_ValidDateString_ReturnsLocalDate() {
        String dateString = "20241107";
        LocalDate result = DateUtils.convertToDate(dateString);

        assertEquals(2024, result.getYear());
        assertEquals(11, result.getMonthValue());
        assertEquals(7, result.getDayOfMonth());
    }

    @Test
    void convertToString_ValidLocalDate_ReturnsFormattedString() {
        LocalDate date = LocalDate.of(2024, 11, 7);
        String result = DateUtils.convertToString(date);

        assertEquals("20241107", result);
    }

    @Test
    void convertToDate_InvalidDateString_ThrowsException() {
        String invalidDateString = "invalid-date";

        assertThrows(Exception.class, () -> {
            DateUtils.convertToDate(invalidDateString);
        });
    }

    @Test
    void convertToString_NullDate_ThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            DateUtils.convertToString(null);
        });
    }
}
