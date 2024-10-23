package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.EmailTemplates;

import com.ems.EventsService.enums.DBRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplatesRepository extends JpaRepository<EmailTemplates, Integer>
{
    Optional<EmailTemplates> findByTemplateNameAndRecStatus(String templateName, DBRecordStatus recStatus);
}

