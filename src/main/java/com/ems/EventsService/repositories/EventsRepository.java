package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.Events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepository extends JpaRepository<Events, Integer>
{
    boolean existsByEventName(String eventName);
}
