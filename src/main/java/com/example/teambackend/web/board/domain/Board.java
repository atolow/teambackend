package com.example.teambackend.web.board.domain;

import com.example.teambackend.web.comment.domain.Comment;
import com.example.teambackend.common.time.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.teambackend.web.user.domain.User;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @Column(nullable = false, length = 30)
    private String title; // 게시글 제목
    @Column(nullable = false, length = 200)
    private String content; // 게시글 내용

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private String ipAddress;



    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;


    @Builder
    public Board(User user, String title, String content, boolean isActive, String ipAddress) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.isActive = isActive;
        this.ipAddress = ipAddress;
    }

    // 게시글 제목과 내용을 수정하는 메서드
    public void updateBoard(String title, String content,String ipAddress) {
        this.title = title;
        this.content = content;
        this.ipAddress = ipAddress;
    }
    public void deactivate(String ipAddress) {
        this.isActive = false;
        this.ipAddress = ipAddress;
    }
}