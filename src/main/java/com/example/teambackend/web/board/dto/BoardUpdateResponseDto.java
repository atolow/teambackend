package com.example.teambackend.web.board.dto;

import com.example.teambackend.web.board.domain.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateResponseDto {

    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String ipAddress;

    // 생성자
    @Builder
    public BoardUpdateResponseDto(Long id, String title, String content, String ipAddress) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.ipAddress = ipAddress;
    }

    // 엔티티 -> DTO 변환 메서드
    public static BoardUpdateResponseDto from(Board board) {
        return BoardUpdateResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .ipAddress(board.getIpAddress())
                .build();
    }
}