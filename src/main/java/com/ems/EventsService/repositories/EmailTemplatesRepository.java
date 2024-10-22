package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.EmailTemplates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailTemplatesRepository extends JpaRepository<EmailTemplates, Integer>
{
    Optional<EmailTemplates> findByTemplateName(String templateName);
}

