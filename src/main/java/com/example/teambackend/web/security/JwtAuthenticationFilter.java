package com.example.teambackend.web.security;

import com.example.teambackend.common.global.dto.GlobalErrorResponse;
import com.example.teambackend.common.global.exception.CustomUserNotFoundException;
import com.example.teambackend.web.redis.RedisBlackListService;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RedisBlackListService redisBlackListService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            // 토큰 존재 여부 확인
            if (token != null) {
                // 로그아웃된 토큰인지 확인
                if (redisBlackListService.isBlacklisted(token)) {
                    throw new JwtException("로그아웃된 토큰입니다.");
                }

                // 유효한 Access Token이면 인증 처리
                if (jwtProvider.validateAccessToken(token)) {
                    String username = jwtProvider.getUsername(token);

                    User user = userRepository.findByUsername(username)
                            .orElseThrow(() -> new RuntimeException("토큰의 사용자 정보를 찾을 수 없습니다."));

                    UserDetailsImp userDetails = new UserDetailsImp(user);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.debug("토큰이 만료되었습니다: {}", e.getMessage());
            // SecurityContext를 비우고 필터 체인 계속 진행 (Spring Security가 401 처리)
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("유효하지 않은 JWT: {}", e.getMessage());
            // SecurityContext를 비우고 필터 체인 계속 진행 (Spring Security가 401 처리)
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        } catch (CustomUserNotFoundException e) {
            log.debug("사용자를 찾을 수 없습니다: {}", e.getMessage());
            // SecurityContext를 비우고 필터 체인 계속 진행 (Spring Security가 401 처리)
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생: {}", e.getMessage());
            // SecurityContext를 비우고 필터 체인 계속 진행 (Spring Security가 401 처리)
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        return null;
    }

    private void setErrorResponse(HttpServletResponse response, String code, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        GlobalErrorResponse error = GlobalErrorResponse.of(code, message);
        String json = new ObjectMapper().writeValueAsString(error);
        response.getWriter().write(json);
    }
}