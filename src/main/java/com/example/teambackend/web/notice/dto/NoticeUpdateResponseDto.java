package com.example.teambackend.web.notice.dto;

import com.example.teambackend.web.notice.domain.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeUpdateResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String clientIp;

    @Builder
    public NoticeUpdateResponseDto(Long id, String title, String content, String clientIp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.clientIp = clientIp;
    }

    public static NoticeUpdateResponseDto from(Notice notice) {
        return NoticeUpdateResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .clientIp(notice.getIpAddress())
                .build();
    }
}