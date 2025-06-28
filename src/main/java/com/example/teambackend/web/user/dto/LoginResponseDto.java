package com.example.teambackend.web.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class LoginResponseDto {


    private String token;

    public LoginResponseDto(String token) {
        this.token = token;
    }
}