package com.example.teambackend.web.user.dto;

import com.example.teambackend.web.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPasswordChangeResponseDto {

    private final Long id;
    private final String username;

    @Builder
    public UserPasswordChangeResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserPasswordChangeResponseDto from(User user) {
        return UserPasswordChangeResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}