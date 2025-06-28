package com.example.teambackend.web.comment.dto;



import com.example.teambackend.web.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentCreateResponseDto {

    private Long id; // 댓글 ID
    private String username; // 작성자의 username
    private String content; // 댓글 내용
    private String clientIp;


    // 생성자
    @Builder
    public CommentCreateResponseDto(Long id, String username, String content, String clientIp) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.clientIp = clientIp;

    }

    public static CommentCreateResponseDto from(Comment comment) {
        return CommentCreateResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .clientIp(comment.getIpAddress())
                .build();
    }
}