package com.example.teambackend.web.board.dto;

import com.example.teambackend.web.board.domain.Board;
import com.example.teambackend.web.comment.domain.Comment;
import com.example.teambackend.web.comment.dto.CommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String author; // 작성자
    private final List<CommentResponseDto> comments;
    private String ipAddress;

    // 생성자
    @Builder
    public BoardResponseDto(Long id, String title, String content, String author, List<CommentResponseDto> comments, String ipAddress) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.comments = comments;
        this.ipAddress = ipAddress;
    }

    // Board 엔티티를 DTO로 변환하는 메서드
    public static BoardResponseDto from(Board board) {
        List<CommentResponseDto> commentDtos = board.getComments().stream()
                .filter(Comment::isActive) // isActive가 true인 댓글만 필터링
                .map(CommentResponseDto::from)
                .collect(Collectors.toList());

        return BoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .author(board.getUser().getUsername())
                .comments(commentDtos)
                .ipAddress(board.getIpAddress())
                .build();
    }
}