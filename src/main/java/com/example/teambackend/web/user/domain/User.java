package com.example.teambackend.web.user.domain;

import com.example.teambackend.common.time.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String username;

    //    @Column(nullable = false, length = 50)
    private String realName;

    @Column(nullable = false, unique = true)
    private String email;

    //    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private Integer cash;

    private double balances;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private String ipAddress;

    @Builder
    public User(String username, String realName, String email, String phoneNumber, String password, Integer cash, Role role, boolean isActive, String ipAddress) {
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.cash = cash;
        this.role = role;
        this.isActive = isActive;
        this.ipAddress = ipAddress;
    }

    public void deactivate(String ipAddress) {
        this.isActive = false;
        this.ipAddress = ipAddress;
    }
    public void changePassword(String encodedPassword,String clientIp) {
        this.password = encodedPassword;
        this.ipAddress = clientIp;
    }

    public void deductBalance(int amount) {
        if (this.cash < amount) {
            throw new IllegalArgumentException("잔액 부족");
        }
        this.cash -= amount;
    }
    public void changeRole(Role role,String clientIp){
        this.role=role;
        this.ipAddress=clientIp;
    }
}