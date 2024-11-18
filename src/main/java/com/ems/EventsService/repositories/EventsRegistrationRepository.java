package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.entity.EventsRegistration;
import com.ems.EventsService.enums.DBRecordStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRegistrationRepository extends JpaRepository<EventsRegistration, Integer>
{
    List<EventsRegistration> findByEventIdAndRecordStatus(Integer eventId, DBRecordStatus recordStatus);

    Optional<EventsRegistration> findByEventIdAndUserIdAndRecordStatus(Integer eventId, Integer userId, DBRecordStatus recordStatus);

    List<EventsRegistration> findByUserIdAndRecordStatus(Integer userId, DBRecordStatus recordStatus);
}


