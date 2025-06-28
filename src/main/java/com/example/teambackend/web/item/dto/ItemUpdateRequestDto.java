package com.example.teambackend.web.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemUpdateRequestDto {

    @NotBlank(message = "아이템 이름은 필수입니다.")
    private String name;

    private String description;

    @Min(value = 1, message = "가격은 1 이상이어야 합니다.")
    private int price;

    @Builder
    public ItemUpdateRequestDto(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public ItemUpdateRequestDto(ItemDetailResponseDto dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
    }
}