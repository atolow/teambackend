package com.example.teambackend.web.comment.controller;

import com.example.teambackend.common.util.IpUtils;
import com.example.teambackend.web.comment.dto.CommentCreateRequestDto;
import com.example.teambackend.web.comment.dto.CommentCreateResponseDto;
import com.example.teambackend.web.comment.dto.CommentUpdateRequestDto;
import com.example.teambackend.web.comment.dto.CommentUpdateResponseDto;
import com.example.teambackend.web.comment.service.CommentService;
import com.example.teambackend.web.security.CustomUserDetails;
import com.example.teambackend.web.security.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<CommentCreateResponseDto> create(
            @PathVariable Long boardId,
            @RequestBody @Valid CommentCreateRequestDto dto,
            @AuthenticationPrincipal UserDetailsImp userDetails,
            HttpServletRequest request) {

        String ip = IpUtils.getClientIp(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.createComment(boardId, dto, userDetails.getUser(),ip));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> update(
            @PathVariable Long commentId,
            @RequestBody @Valid CommentUpdateRequestDto dto,
            @AuthenticationPrincipal UserDetailsImp userDetails,
            HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        return ResponseEntity.ok(
                commentService.updateComment(commentId, dto, userDetails.getUser(),ip));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImp userDetails,
            HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        commentService.deleteComment(commentId, userDetails.getUser(),ip);
        return ResponseEntity.noContent().build();
    }
}

