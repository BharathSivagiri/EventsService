package com.ems.EventsService.entity;

import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.RegistrationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ems_events_registration")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id_fk", referencedColumnName = "id", insertable = false, updatable = false)
    private Events event;

    @ManyToOne
    @JoinColumn(name = "user_id_fk", referencedColumnName = "id", insertable = false, updatable = false)
    private Users users;

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_status")
    private RegistrationStatus registrationStatus;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date", nullable = false)
    private String createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    private DBRecordStatus recordStatus;

    @Column(name = "last_updated_date", nullable = false)
    private String lastUpdatedDate;

    @Column(name = "last_updated_by", nullable = false)
    private String lastUpdatedBy;

}

