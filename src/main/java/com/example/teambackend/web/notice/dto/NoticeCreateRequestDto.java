package com.example.teambackend.web.notice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class NoticeCreateRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용은 필수입니다.")
    private String content;

    // 생성자
    public NoticeCreateRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}