package com.example.teambackend.web.session.domain;

import com.example.teambackend.web.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // 외래 키 설정
    private User user;  // User와 연관 관계 설정

    private String username;
    private String oldIpAddress;
    private String newIpAddress;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;


    public UserSession(User user, String username, String oldIpAddress, String newIpAddress, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.user = user;
        this.username = username;
        this.oldIpAddress = oldIpAddress;
        this.newIpAddress = newIpAddress;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    public void changeOldIpAddress(String oldIpAddress) {
        this.oldIpAddress = oldIpAddress;
    }
    public void changeNewIpAddress(String newIpAddress) {
        this.newIpAddress = newIpAddress;
    }
    public void changeLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
    public void changeLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
}