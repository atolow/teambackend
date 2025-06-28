package com.example.teambackend.web.notice.service;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.notice.domain.Notice;
import com.example.teambackend.web.notice.dto.NoticeCreateRequestDto;
import com.example.teambackend.web.notice.dto.NoticeResponseDto;
import com.example.teambackend.web.notice.dto.NoticeUpdateRequestDto;
import com.example.teambackend.web.notice.repository.NoticeRepository;
import com.example.teambackend.web.user.domain.Role;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.web.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static com.example.teambackend.common.util.EntityValidator.validateIsAdmin;


@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public NoticeResponseDto create(NoticeCreateRequestDto requestDto, User user,String clientIp) {
        validateIsAdmin(user);
        User FindUser = userRepository.findByIdOrElseThrow(user.getId());

        Notice notice = Notice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(FindUser)
                .isActive(true)
                .ipAddress(clientIp)
                .build();
        return NoticeResponseDto.from(noticeRepository.save(notice));
    }

    @Override
    public NoticeResponseDto getById(Long id) {
        Notice notice = noticeRepository.findByIdOrElseThrow(id);
        if (!notice.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }
        return NoticeResponseDto.from(notice);
    }

    @Override
    public Page<NoticeResponseDto> getAll(Pageable pageable) {
        return noticeRepository.findAllByIsActiveTrueOrderByIdDesc(pageable)
                .map(NoticeResponseDto::from);
    }

    @Transactional
    @Override
    public NoticeResponseDto update(Long id, NoticeUpdateRequestDto requestDto, User user, String clientIp) {
        validateIsAdmin(user);
        Notice notice = noticeRepository.findByIdOrElseThrow(id);
        if (!notice.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("수정 권한이 없습니다.");
        }
        if(!notice.isActive()) {
            throw new InvalidCredentialsException("이미 삭제한 게시물 입니다.");
        }
        notice.update(requestDto.getTitle(), requestDto.getContent(),clientIp);
        return NoticeResponseDto.from(notice);
    }

    @Transactional
    @Override
    public void delete(Long id, User user,String clientIp) {
        validateIsAdmin(user);
        Notice notice = noticeRepository.findByIdOrElseThrow(id);
        if (!notice.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("삭제 권한이 없습니다.");
        }
        if(!notice.isActive()) {
            throw new InvalidCredentialsException("이미 삭제한 게시물 입니다.");
        }
        notice.deactivate(clientIp);
    }
}