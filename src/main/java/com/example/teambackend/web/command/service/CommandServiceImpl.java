package com.example.teambackend.web.command.service;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.command.domain.Command;
import com.example.teambackend.web.command.dto.CommandCreateRequestDto;
import com.example.teambackend.web.command.dto.CommandResponseDto;
import com.example.teambackend.web.command.dto.CommandUpdateRequestDto;
import com.example.teambackend.web.command.repository.CommandRepository;
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
public class CommandServiceImpl implements CommandService {

    private final CommandRepository commandRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public CommandResponseDto create(CommandCreateRequestDto requestDto, User user,String clientIp) {
        validateIsAdmin(user);
        User FindUser = userRepository.findByIdOrElseThrow(user.getId());

        Command command = Command.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .user(FindUser)
                .ipAddress(clientIp)
                .isActive(true)
                .build();
        return CommandResponseDto.from(commandRepository.save(command));
    }

    @Override
    public CommandResponseDto getById(Long id) {
        Command command = commandRepository.findByIdOrElseThrow(id);
        if (!command.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }
        return CommandResponseDto.from(command);
    }

    @Override
    public Page<CommandResponseDto> getAll(Pageable pageable) {
        return commandRepository.findAllByIsActiveTrueOrderByIdDesc(pageable)
                .map(CommandResponseDto::from);
    }

    @Transactional
    @Override
    public CommandResponseDto update(Long id, CommandUpdateRequestDto requestDto, User user,String clientIp) {
        validateIsAdmin(user);
        Command command = commandRepository.findByIdOrElseThrow(id);
        if (!command.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("수정 권한이 없습니다.");
        }
        if(!command.isActive()) {
            throw new InvalidCredentialsException("이미 삭제한 게시물 입니다.");
        }
        command.update(requestDto.getTitle(), requestDto.getContent(),clientIp);
        return CommandResponseDto.from(command);
    }

    @Transactional
    @Override
    public void delete(Long id, User user,String clientIp) {
        validateIsAdmin(user);
        Command command = commandRepository.findByIdOrElseThrow(id);
        if (!command.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new InvalidCredentialsException("삭제 권한이 없습니다.");
        }

        if (!command.isActive()) {
            throw new InvalidCredentialsException("이미 삭제된 게시물입니다.");
        }
        command.deactivate(clientIp);

    }

}