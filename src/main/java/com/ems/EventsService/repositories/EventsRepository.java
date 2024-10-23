package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.Events;
import com.ems.EventsService.enums.DBRecordStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Events, Integer>
{
    List<Events> findByEventNameOrEventLocationContainingIgnoreCaseAndRecStatus(String name, String location, DBRecordStatus recStatus);

    List<Events> findByRecStatus(DBRecordStatus recStatus);

    boolean existsByEventNameAndRecStatus(String eventName, DBRecordStatus recStatus);
}
