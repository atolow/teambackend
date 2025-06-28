package com.example.teambackend.web.command.service;

import com.example.teambackend.web.command.dto.CommandCreateRequestDto;
import com.example.teambackend.web.command.dto.CommandResponseDto;
import com.example.teambackend.web.command.dto.CommandUpdateRequestDto;
import org.springframework.data.domain.Page;
import com.example.teambackend.web.user.domain.User;
import org.springframework.data.domain.Pageable;

public interface CommandService {

    CommandResponseDto create(CommandCreateRequestDto requestDto, User user,String clientIp);

    CommandResponseDto getById(Long id);

    Page<CommandResponseDto> getAll(Pageable pageable);

    CommandResponseDto update(Long id, CommandUpdateRequestDto requestDto, User user,String clientIp);

    void delete(Long id, User user,String clientIp);

}