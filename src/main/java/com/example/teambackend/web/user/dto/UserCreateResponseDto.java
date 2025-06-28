package com.example.teambackend.web.user.dto;



import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserCreateResponseDto {

    private final Long id;
    private final String username;
    private final String realName;
    private final String email;
    private final String phoneNumber;
    private final Integer cash;
    private final Role role;
    private final boolean active;
    private final String clientIp;

    @Builder
    public UserCreateResponseDto(Long id, String username, String realName, String email,
                                 String phoneNumber, Integer cash, Role role, boolean active, String clientIp) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cash = cash;
        this.role = role;
        this.active = active;
        this.clientIp = clientIp;
    }

    public static UserCreateResponseDto from(User user) {
        return UserCreateResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .cash(user.getCash())
                .role(user.getRole())
                .active(user.isActive())
                .clientIp(user.getIpAddress())
                .build();
    }
}