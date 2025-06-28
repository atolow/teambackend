package com.example.teambackend.web.session.repository;

import com.example.teambackend.web.session.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByUserUsername(String name);

}
