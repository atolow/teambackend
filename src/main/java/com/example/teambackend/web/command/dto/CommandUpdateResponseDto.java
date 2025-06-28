package com.example.teambackend.web.command.dto;

import com.example.teambackend.web.command.domain.Command;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommandUpdateResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String clientIp;

    @Builder
    public CommandUpdateResponseDto(Long id, String title, String content, String clientIp) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.clientIp = clientIp;
    }

    public static CommandUpdateResponseDto from(Command notice) {
        return CommandUpdateResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .clientIp(notice.getIpAddress())
                .build();
    }
}