package com.example.teambackend.web.notice.repository;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.notice.domain.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    default Notice findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("해당 공지사항이 존재하지 않습니다."));
    }

    Page<Notice> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);

}