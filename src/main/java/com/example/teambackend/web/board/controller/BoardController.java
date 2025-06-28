package com.example.teambackend.web.board.controller;

import com.example.teambackend.common.util.IpUtils;
import com.example.teambackend.web.board.dto.*;
import com.example.teambackend.web.board.service.BoardService;
import com.example.teambackend.web.security.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 🔎 공지사항 목록
    @GetMapping
    public ResponseEntity<Page<BoardResponseDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BoardResponseDto> boards = boardService.getAll(pageable);

        return ResponseEntity.ok(boards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> viewBoard(@PathVariable Long id) {
        BoardResponseDto boardResponseDto = boardService.getById(id);
        return ResponseEntity.ok(boardResponseDto);
    }

    @PostMapping
    public ResponseEntity<BoardCreateResponseDto> create(
            @AuthenticationPrincipal UserDetailsImp userDetails,
            @Valid @RequestBody BoardCreateRequestDto requestDto, HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        BoardCreateResponseDto created = boardService.create(requestDto, userDetails.getUser(),ip);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    // ✏️ 수정 처리
    @PatchMapping("/{id}")
    public ResponseEntity<BoardUpdateResponseDto> edit(@PathVariable Long id,
                                                       @AuthenticationPrincipal UserDetailsImp userDetails,
                                                       @Valid @RequestBody BoardUpdateRequestDto requestDto,
                                                       HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);

        BoardUpdateResponseDto update = boardService.update(id, requestDto, userDetails.getUser(), ip);
        return ResponseEntity.status(HttpStatus.CREATED).body(update);
    }

    // ❌ 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetailsImp userDetails,
                                       HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        boardService.delete(id, userDetails.getUser(),ip);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}