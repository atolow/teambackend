package com.example.teambackend.web.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisBlackListService {

    private final StringRedisTemplate redisTemplate;

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(token, "blacklisted", Duration.ofMillis(expirationMillis));
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(token);
    }
}