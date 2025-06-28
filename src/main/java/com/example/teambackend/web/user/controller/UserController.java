package com.example.teambackend.web.user.controller;

import com.example.teambackend.common.util.IpUtils;
import com.example.teambackend.web.redis.RedisBlackListService;
import com.example.teambackend.web.security.JwtProvider;
import com.example.teambackend.web.security.UserDetailsImp;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.dto.*;
import com.example.teambackend.web.user.service.UserService;
import com.example.teambackend.common.util.UserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RedisBlackListService redisBlackListService;


    @PostMapping("/register")
    public  ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateRequestDto requestDto,
                                                     HttpServletRequest request) {

        String ip = IpUtils.getClientIp(request);
        UserResponseDto user = userService.createUser(requestDto,ip);// 예외 발생 가능
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("잘못된 Authorization 헤더 형식입니다.");
        }

        String token = bearerToken.substring(7);
        long expiration = jwtProvider.getRemainingExpiration(token); // 남은 만료 시간(ms)

        redisBlackListService.blacklistToken(token, expiration);
        return ResponseEntity.ok().body("로그아웃 성공");
    }

    @PostMapping("/change-password")
    public ResponseEntity<UserPasswordChangeResponseDto> changePassword(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                                        @Valid @RequestBody UserPasswordChangeRequestDto requestDto,
                                                                        HttpServletRequest request) {


        String ip = IpUtils.getClientIp(request);
        UserPasswordChangeResponseDto userPasswordChangeResponseDto = userService.updatePassword(userDetails.getUser().getId(), requestDto,ip);
        return ResponseEntity.status(HttpStatus.OK).body(userPasswordChangeResponseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserWithBalanceResponseDto> myInfo(@AuthenticationPrincipal UserDetailsImp userDetails) {
        // 사용자 정보 가져오기
        UserResponseDto user = userService.getUserById(userDetails.getUser().getId());

        // balance 정보 가져오기 (서비스에서 이미 null 체크 및 기본값 0.0 처리)
        Double balance = userService.getBalanceForUser(userDetails.getUser().getUsername());



        return ResponseEntity.ok(new UserWithBalanceResponseDto(user, balance));
    }


    // ❌ 탈퇴 처리
    @PostMapping("/deactivate")
    public ResponseEntity<DeactivateUserResponseDto> deactivate(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                                HttpServletRequest request) {

        String ip = IpUtils.getClientIp(request);
        DeactivateUserResponseDto deactivateUserResponseDto = userService.deactivateUser(userDetails.getUser().getId(),ip);
        return ResponseEntity.status(HttpStatus.OK).body(deactivateUserResponseDto);
    }

    @GetMapping("/balances")
    public ResponseEntity<List<UserBalanceDto>> getBalances() {
        List<UserBalanceDto> balances = userService.getBalanceSortedByBalance();
        return ResponseEntity.ok(balances);
    }




    @PostMapping("/admin/signup")
    public ResponseEntity<UserResponseDto> adminCreatUser(@RequestBody UserCreateRequestDto dto,
                                                          HttpServletRequest request) {

        String ip = IpUtils.getClientIp(request);
        return ResponseEntity.ok(userService.adminSignup(dto,ip));
    }






    @PatchMapping("/users/roles")
    public ResponseEntity<UserResponseDto> grantAdmin(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                      HttpServletRequest request) {

        String ip = IpUtils.getClientIp(request);
        User user = UserUtils.getUser(userDetails);
        UserResponseDto updatedUser = userService.grantAdminRole(user,ip);
        return ResponseEntity.ok(updatedUser);
    }

}
