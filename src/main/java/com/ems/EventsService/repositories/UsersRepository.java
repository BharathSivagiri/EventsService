package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.Users;
import com.ems.EventsService.enums.DBRecordStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer>
{
    Optional<Users>findByCustomNameAndRecStatus(String customName, DBRecordStatus recStatus);

    Optional<Users>findByUserIdAndRecStatus(Integer userId, DBRecordStatus recStatus);
}
