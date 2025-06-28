package com.example.teambackend.web.comment.domain;




import com.example.teambackend.web.board.domain.Board;
import com.example.teambackend.common.time.TimeBaseEntity;
import com.example.teambackend.web.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private String ipAddress;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;



    @Builder
    public Comment(User user, Board board, String content, boolean isActive, String ipAddress) {
        this.user = user;
        this.board = board;
        this.content = content;
        this.isActive = isActive;
        this.ipAddress = ipAddress;
    }
    public void updateContent(String content,String ipAddress) {
        this.content = content;
        this.ipAddress = ipAddress;
    }
    public void deactivate(String ipAddress) {
        this.isActive = false;
        this.ipAddress = ipAddress;
    }
}