package com.example.teambackend.web.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class UserBalanceDto {
    private String username;
    private Double balance; // 소수점 두 자리 포맷된 문자열


    public UserBalanceDto(String username, Double balance) {
        this.username = username;
        this.balance = balance;
    }
}