package com.example.teambackend.web.user.dto;

import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String username;
    private final String realName;
    private final String email;
    private final String phoneNumber;
    private final Integer cash;
    private final Role role;
    private final boolean isActive;
    private final String clientIp;

    @Builder
    public UserResponseDto(Long id, String username, String realName, String email,
                           String phoneNumber, Integer cash, Role role, boolean isActive, String clientIp) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cash = cash;
        this.role = role;
        this.isActive = isActive;
        this.clientIp = clientIp;
    }

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .cash(user.getCash())
                .role(user.getRole())
                .isActive(user.isActive())
                .clientIp(user.getIpAddress())
                .build();
    }
}