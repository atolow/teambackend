package com.example.teambackend.web.notice.domain;

import com.example.teambackend.common.time.TimeBaseEntity;
import com.example.teambackend.web.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private String ipAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public Notice(String title, String content, User user,Boolean isActive, String ipAddress) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.isActive = isActive;
        this.ipAddress = ipAddress;
    }

    public void update(String title, String content,String ipAddress) {
        this.title = title;
        this.content = content;
        this.ipAddress = ipAddress;
    }
    public void deactivate(String ipAddress) {
        isActive = false;
        this.ipAddress = ipAddress;
    }
}