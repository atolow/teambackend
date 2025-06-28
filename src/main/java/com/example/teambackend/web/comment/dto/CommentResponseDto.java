package com.example.teambackend.web.comment.dto;


import com.example.teambackend.web.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponseDto {

    private Long id;
    private String username;
    private String content;
    private String clientIp;

    @Builder
    public CommentResponseDto(Long id, String username, String content, String clientIp) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.clientIp = clientIp;

    }

    // 댓글을 DTO로 변환하는 메서드
    public static CommentResponseDto from(Comment comment) {
        return  CommentResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .clientIp(comment.getIpAddress())
                .build();

    }
}