package com.example.teambackend.web.board.dto;

import com.example.teambackend.web.board.domain.Board;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateResponseDto {

    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String author; // 작성자의 username
    private String ipAddress;

    // 생성자
    @Builder
    public BoardCreateResponseDto(Long id, String title, String content, String author,String ipAddress) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.ipAddress = ipAddress;
    }

    // 엔티티 -> DTO 변환 메서드
    public static BoardCreateResponseDto from(Board board) {
        return BoardCreateResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getUser().getUsername())
                .ipAddress(board.getIpAddress())
                .build();
    }
}