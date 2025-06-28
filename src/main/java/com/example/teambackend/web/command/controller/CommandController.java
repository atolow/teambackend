package com.example.teambackend.web.command.controller;

import com.example.teambackend.common.util.IpUtils;
import com.example.teambackend.web.command.dto.CommandCreateRequestDto;
import com.example.teambackend.web.command.dto.CommandResponseDto;
import com.example.teambackend.web.command.dto.CommandUpdateRequestDto;
import com.example.teambackend.web.command.service.CommandService;
import com.example.teambackend.web.security.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/commands")
public class CommandController {

    private final CommandService commandservice;

    // 🔎 공지사항 목록
    @GetMapping
    public ResponseEntity<Page<CommandResponseDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CommandResponseDto> commands = commandservice.getAll(pageable);

        return ResponseEntity.ok(commands);
    }
    // 🔎 공지사항 상세보기
    @GetMapping("/{id}")
    public ResponseEntity<CommandResponseDto> detail(@PathVariable Long id) {
        CommandResponseDto command = commandservice.getById(id);
        return ResponseEntity.ok(command);
    }

    @PostMapping
    public ResponseEntity<CommandResponseDto> create(
            @AuthenticationPrincipal UserDetailsImp userDetails,
            @Valid @RequestBody CommandCreateRequestDto requestDto,
            HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        CommandResponseDto saved = commandservice.create(requestDto, userDetails.getUser(),ip);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✏️ 수정 처리
    @PatchMapping("/{id}")
    public ResponseEntity<CommandResponseDto> edit(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImp userDetails,
            @Valid @RequestBody CommandUpdateRequestDto requestDto,
            HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        CommandResponseDto updated = commandservice.update(id, requestDto, userDetails.getUser(),ip);
        return ResponseEntity.ok(updated);
    }

    // ❌ 삭제 처리
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImp userDetails,
            HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        commandservice.delete(id, userDetails.getUser(),ip);
        return ResponseEntity.noContent().build();
    }
}