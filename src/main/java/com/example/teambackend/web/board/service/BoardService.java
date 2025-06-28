package com.example.teambackend.web.board.service;

import com.example.teambackend.web.board.dto.*;
import com.example.teambackend.web.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    // 게시글 목록 조회
    Page<BoardResponseDto> getAll(Pageable pageable);

    // 게시글 상세 조회
    BoardResponseDto getById(Long id);

    // 게시글 작성
    BoardCreateResponseDto create(BoardCreateRequestDto boardCreateRequestDto, User user,String clientIp);

    // 게시글 수정
    BoardUpdateResponseDto update(Long id, BoardUpdateRequestDto boardUpdateRequestDto, User user, String clientIp);

    // 게시글 삭제
    void delete(Long id, User user,String clientIp);

}