package com.ems.EventsService.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class EventsModel {
    private String eventId;

    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Name must contain only letters, numbers and spaces")
    @Size(min = 2, max = 30, message = "Name must be 10-30 characters long")
    private String eventName;

    private String eventDescription;

    @Pattern(regexp = "^\\d{4}\\d{2}\\d{2}$", message = "Event date must be in the format YYYYMMDD")
    private String eventDate;

    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Location must contain only letters and spaces")
    @Size(min = 2, max = 30, message = "Location must be 10-30 characters long")
    private String eventLocation;

    @Min(value = 0, message = "Capacity must be a non-negative number")
    @Max(value = 1000000, message = "Capacity must not exceed 1,000,000")
    private Integer eventCapacity;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "Fee must be a valid number with up to 2 decimal places")
    private String eventFee;

    @Pattern(regexp = "^(opened|closed|cancelled)$", message = "Type must be one of the following: opened, closed, cancelled")
    private String eventStatus;

    private String recStatus;
    private String createdBy;
    private String createdDate;
    private String updatedDate;
    private String updatedBy;
}
