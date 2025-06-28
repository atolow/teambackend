package com.example.teambackend.web.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    @NotBlank(message = "제목은 필수입니다.")
    private String title; // 게시글 제목
    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 게시글 내용

    // 생성자
    public BoardUpdateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}