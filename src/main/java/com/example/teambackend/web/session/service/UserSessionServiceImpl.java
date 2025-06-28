package com.example.teambackend.web.session.service;

import com.example.teambackend.web.session.domain.UserSession;
import com.example.teambackend.web.session.repository.UserSessionRepository;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSessionServiceImpl implements UserSessionService {

    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    // UserSession을 업데이트하는 메서드
    @Transactional
    @Override
    public void updateUserSession(String username, String newIpAddress, LocalDateTime loginTime) {
        // 기존 세션이 있는지 확인
        Optional<UserSession> existingSession = userSessionRepository.findByUserUsername(username);

        if (existingSession.isPresent()) {
            UserSession userSession = existingSession.get();
            // 기존 세션이 있으면 세션을 갱신 (IP와 로그인 시간 업데이트)
            userSession.changeLoginTime(loginTime);
            userSession.changeNewIpAddress(newIpAddress);
            userSessionRepository.save(userSession);
        } else {
            // 기존 세션이 없으면 새로운 세션 생성
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            UserSession userSession = new UserSession(user, username,null, newIpAddress, loginTime, null);
            userSessionRepository.save(userSession);
        }
        // 세션 저장
    }

    // 로그아웃 세션을 업데이트하는 메서드
    @Transactional
    @Override
    public void updateLogoutSession(String username, String ipAddress, LocalDateTime logoutTime) {
        // 기존 세션이 있는지 확인
        Optional<UserSession> existingSession = userSessionRepository.findByUserUsername(username);

        if (existingSession.isPresent()) {
            UserSession userSession = existingSession.get();
            // 기존 세션이 있으면 세션을 갱신 (IP와 로그인 시간 업데이트)
            userSession.changeLogoutTime(logoutTime);
            userSession.changeOldIpAddress(ipAddress);
            userSessionRepository.save(userSession);
        } else {
            // 기존 세션이 없으면 새로운 세션 생성
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            UserSession userSession = new UserSession(user,username, null, ipAddress, logoutTime, null);
            userSessionRepository.save(userSession);
        }
        // 세션 저장
    }
}