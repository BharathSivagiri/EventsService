package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.enums.DBRecordStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepository extends JpaRepository<Events, Integer>
{
    Optional<Events> findByEventName(String eventName);

    boolean existsByEventName(String eventName);

    List<Events> findByEventNameContainingIgnoreCase(String searchKey);

    List<Events> findByEventNameContainingIgnoreCaseAndRecStatus(String keyword, DBRecordStatus recStatus);

}
