package com.example.teambackend.web.item.dto;



import com.example.teambackend.web.item.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemUpdateResponseDto {

    private final Long id;
    private final String name;
    private final String description;
    private final int price;
    private final boolean isSold;

    @Builder
    public ItemUpdateResponseDto(Long id, String name, String description, int price, boolean isSold) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isSold = isSold;
    }

    public static ItemUpdateResponseDto from(Item item) {
        return ItemUpdateResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .isSold(item.isSold())
                .build();
    }
}