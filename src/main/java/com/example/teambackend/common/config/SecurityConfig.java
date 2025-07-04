package com.example.teambackend.common.config;


import com.example.teambackend.web.redis.RedisBlackListService;
import com.example.teambackend.web.security.JwtAuthenticationFilter;
import com.example.teambackend.web.security.JwtProvider;
import com.example.teambackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity // @PreAuthorize 등 사용 가능하게 함
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RedisBlackListService redisBlackListService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .logout(logout -> logout.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 기반은 무상태
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/register",
                                "/admin/signup",
                                "/login",
                                "/logout",
                                "/refresh",
                                "/minecraft/online-users",
                                "/api/minecraft/online-users",
                                "/notices",
                                "/notices/**",
                                "/commands",
                                "/commands/**",
                                "/boards",
                                "/boards/**",
                                "/error",
                                "/error/**",
                                "/css/**",
                                "/js/**"
                                ).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().hasRole("USER")
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider, userRepository, redisBlackListService),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((req, res, ex) -> {
                            // CORS 헤더 추가
                            res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                            res.setHeader("Access-Control-Allow-Credentials", "true");
                            
                            // 인증 실패 (토큰 없음/유효하지 않음)
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("""
                                {
                                  "error": {
                                    "code": "UNAUTHORIZED",
                                    "message": "로그인이 필요합니다."
                                  }
                                }
                            """);
                        })
                        .accessDeniedHandler((req, res, ex) -> {
                            // CORS 헤더 추가
                            res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
                            res.setHeader("Access-Control-Allow-Credentials", "true");
                            
                            // 인가 실패 (권한 부족)
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("""
                                {
                                  "error": {
                                    "code": "ACCESS_DENIED",
                                    "message": "접근 권한이 없습니다."
                                  }
                                }
                            """);
                        })
                )
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",  // React 개발 서버
            "http://moonserver.kr"    // 운영 도메인
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 로그인 인증에 필요 (FormLogin 사용 안하더라도 내부적으로 필요함)

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
