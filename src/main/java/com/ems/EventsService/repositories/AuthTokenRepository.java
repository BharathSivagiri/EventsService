package com.ems.EventsService.repositories;

import com.ems.EventsService.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Integer> {
    Optional<AuthToken> findByAuthToken(String authToken);
}

