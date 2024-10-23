package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.AuthToken;
import com.ems.EventsService.enums.DBRecordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer>
{
    Optional<AuthToken> findByAuthTokenAndRecStatus(String authToken, DBRecordStatus recStatus);
    List<AuthToken> findByResetTimeBeforeAndRecStatus(LocalDateTime resetTime, DBRecordStatus recStatus);
}

