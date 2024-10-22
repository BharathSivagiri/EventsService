package com.ems.EventsService.entity;

import com.ems.EventsService.enums.DBRecordStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ems_email_templates")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplates
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Column(name = "template_code", nullable = false)
    private String templateCode;

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
