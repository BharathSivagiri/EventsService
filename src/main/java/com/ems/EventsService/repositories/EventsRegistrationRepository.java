package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.EventsRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRegistrationRepository extends JpaRepository<EventsRegistration, Integer>
{

}
