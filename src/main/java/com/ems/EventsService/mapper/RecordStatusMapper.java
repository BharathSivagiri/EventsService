package com.ems.EventsService.mapper;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.enums.DBRecordStatus;

public class RecordStatusMapper {

    public static void setInactiveStatus(AuthToken authToken) {
        authToken.setRecStatus(DBRecordStatus.INACTIVE);
    }
}
