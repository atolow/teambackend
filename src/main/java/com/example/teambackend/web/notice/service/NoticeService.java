package com.example.teambackend.web.notice.service;

import com.example.teambackend.web.notice.dto.NoticeCreateRequestDto;
import com.example.teambackend.web.notice.dto.NoticeResponseDto;
import com.example.teambackend.web.notice.dto.NoticeUpdateRequestDto;
import com.example.teambackend.web.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeService {

    NoticeResponseDto create(NoticeCreateRequestDto requestDto, User user,String clientIp);

    Page<NoticeResponseDto> getAll(Pageable pageable);

    NoticeResponseDto update(Long id, NoticeUpdateRequestDto requestDto, User user,String clientIp);

    void delete(Long id, User user,String clientIp);

    NoticeResponseDto getById(Long id);
}