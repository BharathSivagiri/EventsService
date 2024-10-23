package com.ems.EventsService.entity;

import com.ems.EventsService.enums.DBRecordStatus;
import com.ems.EventsService.enums.UsersType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ems_users_details")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int userId;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "custom_name", nullable = false)
    private String customName;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_email", nullable = false)
    private String email;

    @Column(name = "user_mobile", nullable = false)
    private String mobile;

    @Column(name = "user_address", nullable = false)
    private String address;

    @Column(name = "user_account_no", nullable = false)
    private String account;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UsersType userType;

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
