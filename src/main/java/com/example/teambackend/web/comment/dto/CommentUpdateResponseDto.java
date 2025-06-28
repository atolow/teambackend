package com.example.teambackend.web.comment.dto;




import com.example.teambackend.web.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentUpdateResponseDto {

    private Long id;         // 수정된 댓글의 ID
    private String username; // 작성자의 이름
    private String content;  // 수정된 댓글 내용
    private String clientIp;

    @Builder
    public CommentUpdateResponseDto(Long id, String username, String content, String clientIp) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.clientIp = clientIp;
    }

    // Comment 엔티티를 받아 DTO로 변환하는 메서드
    public static CommentUpdateResponseDto from(Comment comment) {
        return CommentUpdateResponseDto.builder()
                .id(comment.getId())
                .username(comment.getUser().getUsername())
                .content(comment.getContent())
                .clientIp(comment.getIpAddress())
                .build();
    }
}