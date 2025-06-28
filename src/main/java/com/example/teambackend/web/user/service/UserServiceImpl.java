package com.example.teambackend.web.user.service;

import com.example.teambackend.common.global.exception.CustomAccessDeniedException;
import com.example.teambackend.common.global.exception.CustomUserNotFoundException;
import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.common.global.exception.UserAlreadyExistsException;
import com.example.teambackend.web.security.JwtProvider;
import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.dto.*;
import com.example.teambackend.web.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Transactional
    @Override
    public UserResponseDto createUser(UserCreateRequestDto request,String clientIp) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("이미 가입된 사용자입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
//                .realName(request.getRealName())
                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .cash(0)                    // 회원가입 시 잔액은 0원
                .role(Role.USER)        // 기본 역할은 NEWBIE
                .isActive(true)
                .ipAddress(clientIp)
                .build();


        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    @Transactional
    @Override
    public UserResponseDto adminSignup(UserCreateRequestDto request,String clientIp) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("이미 가입된 사용자입니다.");
        }

        User user = User.builder()
                .username(request.getUsername())
//                .realName(request.getRealName())
                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .cash(0)                    // 회원가입 시 잔액은 0원
                .role(Role.ADMIN)        // 기본 역할은 NEWBIE
                .isActive(true)
                .ipAddress(clientIp)
                .build();


        userRepository.save(user);

        return UserResponseDto.from(user);
    }

    @Transactional
    @Override
    public UserPasswordChangeResponseDto updatePassword(Long userId, UserPasswordChangeRequestDto requestDto,String clientIp) {
        User user = userRepository.findByIdOrElseThrow(userId);

        if (!user.isActive()) {
            throw new InvalidCredentialsException("이미 탈퇴한 유저입니다.");
        }
        // 기존 비밀번호 일치 확인
        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("기존 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 일치 확인
        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new InvalidCredentialsException("새 비밀번호 확인이 일치하지 않습니다.");
        }

        // 변경
        String encodedPassword = passwordEncoder.encode(requestDto.getNewPassword());
        user.changePassword(encodedPassword,clientIp);

        return UserPasswordChangeResponseDto.from(user);
    }

    @Transactional
    @Override
    public UserResponseDto getUserById(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        return UserResponseDto.from(user);
    }


    @Transactional
    @Override
    public DeactivateUserResponseDto deactivateUser(Long userId,String clientIp) {
        User user = userRepository.findByIdOrElseThrow(userId);
        if (!user.isActive()) {
            throw new InvalidCredentialsException("이미 탈퇴한 유저입니다.");
        }
        user.deactivate(clientIp);
        return DeactivateUserResponseDto.from(user);
    }


    @Transactional
    @Override
    // 사용자 이름으로 balance 값을 가져오는 메서드
    public Double getBalanceForUser(String username) {
        Double balance = userRepository.findBalanceByUsername(username);
        return balance != null ? balance : 0.0;  // balance가 null이면 0.0을 반환
    }


    @Transactional
    @Override
    public List<UserBalanceDto> getBalanceSortedByBalance() {
        List<Object[]> results = userRepository.findBalanceFromCMIUsers();
        DecimalFormat df = new DecimalFormat("#.##");

        return results.stream()
                .map(result -> {
                    String username = (String) result[0];
                    Double balance = (Double) result[1];
                    String formattedBalance = df.format(balance);
                    return new UserBalanceDto(username, formattedBalance);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomUserNotFoundException("아이디 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("비밀번호가 올바르지 않습니다.");
        }

        if(!user.isActive()) {
            throw new InvalidCredentialsException("탈퇴한 회원입니다.");
        }

        String token = jwtProvider.generateToken(user.getUsername(), user.getRole());
        return new LoginResponseDto(token);
    }

    @Transactional
    @Override
    public UserResponseDto grantAdminRole(User authentication,String clientIp) {
        // 요청한 사용자 확인
        User requester = userRepository.findByUsername(authentication.getUsername())
                .orElseThrow(() -> new CustomUserNotFoundException("요청자 계정을 찾을 수 없습니다."));
        if (!requester.getRole().equals(Role.ADMIN)) {
            throw new CustomAccessDeniedException("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
        }
        // 대상 유저 찾기
        User target = userRepository.findById(requester.getId())
                .orElseThrow(() -> new CustomUserNotFoundException("대상 사용자를 찾을 수 없습니다."));

        // 관리자 권한 없으면 예외
        target.changeRole(Role.ADMIN,clientIp);

        return UserResponseDto.from(target);
    }

}
