package com.example.teambackend.common.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && ip.contains(",")) {
            // 여러 IP가 있을 경우 첫 번째가 클라이언트 IP
            ip = ip.split(",")[0].trim();
        }

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }
}
