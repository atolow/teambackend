package com.example.teambackend.web.session.security;

import com.example.teambackend.web.session.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
@RequiredArgsConstructor
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final UserSessionService userSessionService;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException {
        // 로그아웃 시 사용자 이름과 IP 주소를 가져옵니다.
        String username = authentication != null ? authentication.getName() : null;
        String ipAddress = request.getRemoteAddr(); // 클라이언트 IP 주소 가져오기

        LocalDateTime loginTime = LocalDateTime.now();

        if (username != null) {
            // 로그아웃 시간 저장
            userSessionService.updateLogoutSession(username, ipAddress, loginTime);
        }

        // 로그아웃 후 리디렉션 할 URL 설정
        response.sendRedirect("/home");  // 로그아웃 후 리디렉트 경로
    }
}