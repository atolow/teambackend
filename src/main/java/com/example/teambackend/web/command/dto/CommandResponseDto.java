package com.example.teambackend.web.command.dto;

import com.example.teambackend.web.command.domain.Command;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommandResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final String clientIp;

    @Builder
    public CommandResponseDto(Long id, String title, String content, String author, String clientIp) {
        this.id = id;
        this.title = title;
        this.content = content; // ✅ 추가
        this.author = author;
        this.clientIp = clientIp;
    }

    public static CommandResponseDto from(Command notice) {
        return CommandResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent()) // ✅ 추가
                .author(notice.getUser().getUsername())
                .clientIp(notice.getIpAddress())
                .build();
    }
}