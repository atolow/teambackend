package com.example.teambackend.common.util;


import com.example.teambackend.common.global.exception.UnauthorizedException;
import com.example.teambackend.web.security.UserDetailsImp;
import com.example.teambackend.web.user.domain.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserUtils {

    public static User getUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("아이디가 존재하지 않습니다.");
        }

        return ((UserDetailsImp) userDetails).getUser(); // ✅ 형변환은 유지
    }
}