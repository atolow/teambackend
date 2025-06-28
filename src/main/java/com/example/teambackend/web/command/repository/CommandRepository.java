package com.example.teambackend.web.command.repository;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.command.domain.Command;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandRepository extends JpaRepository<Command, Long> {

    default Command findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("해당 명령어가 존재하지 않습니다."));
    }

    Page<Command> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
}