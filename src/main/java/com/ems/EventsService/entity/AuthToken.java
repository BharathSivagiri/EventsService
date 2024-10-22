package com.ems.EventsService.entity;

import com.ems.EventsService.enums.DBRecordStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ems_auth_token")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id_auth")
    private int userIdAuth;

    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;

    @Column(name = "valid_for", nullable = false)
    private Integer validFor;

    @Column(name = "reset_time", nullable = false)
    private LocalDateTime resetTime;

    @Column(name = "auth_token", nullable = false, length = 50)
    private String authToken;

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

    @ManyToOne
    @JoinColumn(name = "user_id_auth", referencedColumnName = "id", insertable = false, updatable = false)
    private Users users;
}
