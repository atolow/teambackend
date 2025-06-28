package com.example.teambackend.web.notice.dto;

import com.example.teambackend.web.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final String clientIp;

    @Builder
    public NoticeResponseDto(Long id, String title, String content, String author,  String clientIp) {
        this.id = id;
        this.title = title;
        this.content = content; // ✅ 추가
        this.author = author;
        this.clientIp = clientIp;
    }

    public static NoticeResponseDto from(Notice notice) {
        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent()) // ✅ 추가
                .author(notice.getUser().getUsername())
                .clientIp(notice.getIpAddress())
                .build();
    }
}