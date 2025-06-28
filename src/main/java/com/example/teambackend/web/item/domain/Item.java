package com.example.teambackend.web.item.domain;


import com.example.teambackend.common.time.TimeBaseEntity;
import com.example.teambackend.web.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private boolean isSold = false;

    @Builder
    public Item(String name, String description, int price, User user) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.user = user;
    }

    public void markAsSold() {
        this.isSold = true;
    }

    public void update(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}