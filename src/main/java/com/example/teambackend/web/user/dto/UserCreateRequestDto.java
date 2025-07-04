package com.example.teambackend.web.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 6, max = 15, message = "아이디는 6-15자여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "아이디는 영문자만 허용됩니다.")
    private String username;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;


    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,20}$",
            message = "비밀번호는 8-20자, 영문/숫자/특수문자 포함")
    private String password;

}