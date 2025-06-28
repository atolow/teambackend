package com.example.teambackend.web.user.dto;

import com.example.teambackend.web.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithBalanceResponseDto {

    private UserResponseDto user;
    private Double balance;

    public static UserWithBalanceResponseDto from(User user, Double balance) {
        UserWithBalanceResponseDto dto = new UserWithBalanceResponseDto();
        dto.user = UserResponseDto.from(user);
        dto.balance = balance;
        return dto;
    }
}
