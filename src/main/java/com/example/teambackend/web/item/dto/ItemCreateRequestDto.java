package com.example.teambackend.web.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
public class ItemCreateRequestDto {

    @NotBlank(message = "아이템 이름은 필수입니다.")
    private String name;

    private String description;

    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    private int price;

    @Builder
    public ItemCreateRequestDto(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}