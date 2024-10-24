package com.ems.EventsService.utility;

import com.ems.EventsService.utility.constants.AppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT);

    public static LocalDate convertToDate(String dateString)
    {
        return LocalDate.parse(dateString, FORMATTER);
    }

    public static String convertToString(LocalDate date)
    {
        return date.format(FORMATTER);
    }
}