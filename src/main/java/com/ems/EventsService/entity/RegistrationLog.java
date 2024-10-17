package com.ems.EventsService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "registration_error_log")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationLog
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;
}
