package com.ems.EventsService.enums;

import lombok.Getter;

@Getter
public enum UsersType
{
    ADMIN("admin"),
    PARTICIPANT("participant");

    private final String usersType;

    UsersType(String usersType)
    {
        this.usersType = usersType;
    }

    public static UsersType fromString(String userTypeStr)
    {
        for (UsersType type : UsersType.values())
        {
            if (type.getUsersType().equalsIgnoreCase(userTypeStr))
            {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid user status: " + userTypeStr);
    }
}
