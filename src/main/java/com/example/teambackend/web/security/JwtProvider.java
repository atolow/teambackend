package com.example.teambackend.web.security;

import com.example.teambackend.common.global.exception.InvalidTokenException;
import com.example.teambackend.web.user.domain.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKeyString;
    
    @Value("${jwt.refresh-secret-key}")
    private String refreshSecretKeyString;

    private Key secretKey;
    private Key refreshSecretKey;

    @Value("${jwt.access-token-expiration}")
    private long ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000; // 15분
    
    @Value("${jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7일

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        this.refreshSecretKey = Keys.hmacShaKeyFor(refreshSecretKeyString.getBytes());
    }

    /**
     * Access Token 발급
     */
    public String generateAccessToken(String username, Role role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .claim("type", "access")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Refresh Token 발급
     */
    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(refreshSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 사용자 지정 만료 시간으로 토큰 발급
     */
    public String generateToken(String username, Role role, long expireMillis) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public long getRemainingExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

    /**
     * Access Token 유효성 검사
     */
    public boolean validateAccessToken(String token) {
        try {
            Claims claims = parseClaimsWithKey(token, secretKey);
            return !claims.getExpiration().before(new Date()) && "access".equals(claims.get("type"));
        } catch (ExpiredJwtException e) {
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("유효하지 않은 Access Token입니다.");
        }
    }
    
    /**
     * Refresh Token 유효성 검사
     */
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = parseClaimsWithKey(token, refreshSecretKey);
            return !claims.getExpiration().before(new Date()) && "refresh".equals(claims.get("type"));
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("유효하지 않은 Refresh Token입니다.");
        }
    }

    /**
     * 토큰에서 username 추출
     */
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * 토큰에서 Role 추출
     */
    public Role getRole(String token) {
        try {
            String roleString = parseClaims(token).get("role", String.class);
            return Role.valueOf(roleString);
        } catch (Exception e) {
            throw new InvalidTokenException("JWT에서 role을 추출할 수 없습니다.");
        }
    }

    /**
     * Claims 파싱 (공통 처리용)
     */
    private Claims parseClaims(String token) {
        return parseClaimsWithKey(token, secretKey);
    }
    
    /**
     * 특정 키로 Claims 파싱
     */
    private Claims parseClaimsWithKey(String token, Key key) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("JWT 파싱 실패");
        }
    }
    
    /**
     * Refresh Token에서 username 추출
     */
    public String getUsernameFromRefreshToken(String token) {
        Claims claims = parseClaimsWithKey(token, refreshSecretKey);
        return claims.getSubject();
    }
    public Key getSecretKey() {
        return this.secretKey;
    }
}