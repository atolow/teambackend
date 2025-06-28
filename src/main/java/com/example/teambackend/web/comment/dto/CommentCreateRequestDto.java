package com.example.teambackend.web.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter

@NoArgsConstructor
public class CommentCreateRequestDto {

    private String content; // 댓글 내용

    // 생성자
    public CommentCreateRequestDto(String content) {
        this.content = content;
    }
}