package com.example.teambackend.web.session.dto;

import com.example.teambackend.web.session.domain.UserSession;
import com.example.teambackend.web.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Builder
public class UserSessionDTO {

    private Long id;
    private User user;  // 연관된 User 객체
    private String username;
    private String oldIpAddress;
    private String newIpAddress;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;


    public UserSessionDTO(Long id, User user, String username, String oldIpAddress, String newIpAddress, LocalDateTime loginTime, LocalDateTime logoutTime) {
        this.id = id;
        this.user = user;
        this.username = username;
        this.oldIpAddress = oldIpAddress;
        this.newIpAddress = newIpAddress;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
    }

    // 엔티티를 DTO로 변환하는 메서드
    public static UserSessionDTO from(UserSession userSession) {
        return new UserSessionDTO(
                userSession.getId(),
                userSession.getUser(),
                userSession.getUsername(),
                userSession.getOldIpAddress(),
                userSession.getNewIpAddress(),
                userSession.getLoginTime(),
                userSession.getLogoutTime()
        );
    }
}