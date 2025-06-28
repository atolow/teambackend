package com.example.teambackend.web.board.repository;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {


    default Board findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("해당 board가 존재하지 않습니다."));
    }

    Page<Board> findAllByIsActiveTrueOrderByIdDesc(Pageable pageable);
}