package com.example.teambackend.web.notice.controller;

import com.example.teambackend.common.util.IpUtils;
import com.example.teambackend.web.notice.dto.NoticeCreateRequestDto;
import com.example.teambackend.web.notice.dto.NoticeResponseDto;
import com.example.teambackend.web.notice.dto.NoticeUpdateRequestDto;
import com.example.teambackend.web.notice.service.NoticeService;
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
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    // 🔎 공지사항 목록
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<NoticeResponseDto> notices = noticeService.getAll(pageable);

        return ResponseEntity.status(HttpStatus.CREATED).body(notices);
    }

    // 🔎 공지사항 상세보기
    @GetMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> detail(@PathVariable Long id) {
        NoticeResponseDto notice = noticeService.getById(id);
        return ResponseEntity.ok(notice);
    }


    @PostMapping
    public ResponseEntity<NoticeResponseDto> create(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                    @Valid @RequestBody NoticeCreateRequestDto requestDto,
                                                    HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        NoticeResponseDto response = noticeService.create(requestDto, userDetails.getUser(),ip);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<NoticeResponseDto> update(@PathVariable Long id,
                                                    @AuthenticationPrincipal UserDetailsImp userDetails,
                                                    @Valid @RequestBody NoticeUpdateRequestDto requestDto,
                                                    HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        NoticeResponseDto updatedNotice = noticeService.update(id, requestDto, userDetails.getUser(),ip);
        return ResponseEntity.ok(updatedNotice);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal UserDetailsImp userDetails,
                                       HttpServletRequest request) {
        String ip = IpUtils.getClientIp(request);
        noticeService.delete(id, userDetails.getUser(),ip);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}