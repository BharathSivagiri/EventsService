package com.ems.EventsService.enums;

public enum RegistrationStatus {
    REGISTERED("registered"),
    CANCELLED("cancelled"),;

    private final String status;

    RegistrationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static RegistrationStatus fromString(String status) {
        for (RegistrationStatus rs : RegistrationStatus.values()) {
            if (rs.getStatus().equalsIgnoreCase(status)) {
                return rs;
            }
        }
        throw new IllegalArgumentException("Invalid registration status: " + status);
    }
}

