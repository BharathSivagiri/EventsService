package com.ems.EventsService.enums;

public enum DBRecordStatus
{
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String dbStatus;

    DBRecordStatus(String dbStatus)
    {
        this.dbStatus = dbStatus;
    }

    public String getDBStatus()
    {
        return dbStatus;
    }

    public static DBRecordStatus fromString(String dbStatus)
    {
        for (DBRecordStatus status : DBRecordStatus.values())
        {
            if (status.dbStatus.equalsIgnoreCase(dbStatus))
            {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with text " + dbStatus + " found");
    }
}
