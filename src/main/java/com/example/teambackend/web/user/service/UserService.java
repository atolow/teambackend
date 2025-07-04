package com.example.teambackend.web.user.service;

import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.dto.*;
import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserCreateRequestDto request,String clientIp);

    UserPasswordChangeResponseDto updatePassword(Long userId, UserPasswordChangeRequestDto requestDto,String clientIp);

    UserResponseDto getUserById(Long userId);

    DeactivateUserResponseDto deactivateUser(Long userId,String clientIp);


    UserResponseDto adminSignup(UserCreateRequestDto request,String clientIp);
    LoginResponseDto login(LoginRequestDto request);
    String generateRefreshToken(String username);
    TokenRefreshResponseDto refreshAccessToken(String refreshToken);
    UserResponseDto grantAdminRole(User userDetails,String clientIp);

    Double getBalanceForUser(String username);

    List<UserBalanceDto> getBalanceSortedByBalance();
}
