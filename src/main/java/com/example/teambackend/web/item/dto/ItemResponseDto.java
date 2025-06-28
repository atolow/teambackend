package com.example.teambackend.web.item.dto;



import com.example.teambackend.web.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResponseDto {

    private final Long id;
    private final String name;
    private final int price;
    private final boolean isSold;

    @Builder
    public ItemResponseDto(Long id, String name, int price, boolean isSold) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isSold = isSold;
    }

    public static ItemResponseDto from(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .isSold(item.isSold())
                .build();
    }
}