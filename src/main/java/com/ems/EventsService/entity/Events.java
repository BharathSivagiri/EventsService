package com.ems.EventsService.entity;

import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.EventStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ems_events_details")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Events
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int eventId;

    @Column(name = "event_name", nullable = false)
    private String eventName;

    @Column(name = "event_description", nullable = false)
    private String eventDescription;

    @Column(name = "event_date", nullable = false)
    private String eventDate;

    @Column(name = "event_location", nullable = false)
    private String eventLocation;

    @Column(name = "event_capacity", nullable = false)
    private int eventCapacity;

    @Column(name = "event_fee", nullable = false)
    private double eventFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private EventStatus eventStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    private DBRecordStatus recStatus;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "last_updated_by")
    private String updatedBy;

    @Column(name = "last_updated_date")
    private String updatedDate;
}
