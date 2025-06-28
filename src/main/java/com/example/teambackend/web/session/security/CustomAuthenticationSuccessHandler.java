package com.example.teambackend.web.session.security;

import com.example.teambackend.web.session.service.UserSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserSessionService userSessionService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 로그인한 사용자의 username과 IP 주소를 가져옵니다.
        String username = authentication.getName();
        String ipAddress = request.getRemoteAddr();
        LocalDateTime loginTime = LocalDateTime.now();

        // 로그인 시 UserSession을 갱신하거나 새로 생성
         userSessionService.updateUserSession(username, ipAddress, loginTime);

        // 세션 업데이트 후 리디렉션 (예: 홈 페이지로 이동)
        response.sendRedirect("/home");  // 로그인 성공 후 리디렉트 경로
    }
}