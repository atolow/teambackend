package com.example.teambackend.web.item.controller;

import com.example.teambackend.web.item.dto.*;
import com.example.teambackend.web.item.service.ItemService;
import com.example.teambackend.web.security.CustomUserDetails;
import com.example.teambackend.web.security.UserDetailsImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemResponseDto>> list(@AuthenticationPrincipal UserDetailsImp userDetails) {
        List<ItemResponseDto> items = itemService.getAll(userDetails.getUser());
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDetailResponseDto> detail(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                  @PathVariable Long id) {
        ItemDetailResponseDto item = itemService.getById(id, userDetails.getUser());
        return ResponseEntity.ok(item);
    }

    @PostMapping("/create")
    public ResponseEntity<ItemCreateResponseDto> create(@AuthenticationPrincipal UserDetailsImp userDetails,
                                                        @Valid @RequestBody ItemCreateRequestDto dto) {
        ItemCreateResponseDto saved = itemService.create(dto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserDetailsImp userDetails,
                                       @PathVariable Long id,
                                       @Valid @RequestBody ItemUpdateRequestDto dto) {
        itemService.update(id, dto, userDetails.getUser());
        return ResponseEntity.ok().build(); // 또는 noContent()
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetailsImp userDetails,
                                       @PathVariable Long id) {
        itemService.delete(id, userDetails.getUser());
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}