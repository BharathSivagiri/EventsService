package com.ems.EventsService.enums;

import com.ems.EventsService.utility.constants.AppConstants;
import com.ems.EventsService.utility.constants.ErrorMessages;

public enum RegistrationStatus
{
    REGISTERED("registered"),
    CANCELLED("cancelled"),;

    private final String status;

    RegistrationStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }

    public static RegistrationStatus fromString(String status)
    {
        for (RegistrationStatus rs : RegistrationStatus.values())
        {
            if (rs.getStatus().equalsIgnoreCase(status))
            {
                return rs;
            }
        }
        throw new IllegalArgumentException(ErrorMessages.INVALID_REG_STATUS);
    }
}

