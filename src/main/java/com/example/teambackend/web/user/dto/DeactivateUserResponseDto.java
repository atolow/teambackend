package com.example.teambackend.web.user.dto;

import com.example.teambackend.web.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class DeactivateUserResponseDto {
    private Long id;
    private boolean deactivated;
    private String clientIp;

    @Builder
    public DeactivateUserResponseDto(Long id, boolean deactivated, String clientIp) {
        this.id = id;

        this.deactivated = deactivated;
        this.clientIp = clientIp;
    }

    public static DeactivateUserResponseDto from(User user) {
        return DeactivateUserResponseDto.builder()
                .id(user.getId())
                .deactivated(user.isActive())
                .clientIp(user.getIpAddress())
                .build();
    }
}