package com.example.teambackend.web.session.service;

import java.time.LocalDateTime;


public interface UserSessionService {
    void updateUserSession(String username, String newIpAddress, LocalDateTime loginTime);
    void updateLogoutSession(String username, String ipAddress, LocalDateTime logoutTime);
}