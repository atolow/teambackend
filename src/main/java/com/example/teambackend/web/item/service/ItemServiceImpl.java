package com.example.teambackend.web.item.service;


import com.example.teambackend.web.item.domain.Item;
import com.example.teambackend.web.item.dto.*;
import com.example.teambackend.web.item.repository.ItemRepository;
import com.example.teambackend.web.user.domain.User;
import com.example.teambackend.common.util.EntityValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import static com.example.teambackend.common.util.EntityValidator.validateIsAdmin;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    @Transactional
    @Override
    public ItemCreateResponseDto create(ItemCreateRequestDto requestDto, User user) {
        validateIsAdmin(user);
        Item item = Item.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .user(user)
                .build();

        return ItemCreateResponseDto.from(itemRepository.save(item));
    }
    @Transactional
    @Override
    public ItemUpdateResponseDto update(Long id, ItemUpdateRequestDto requestDto, User user) {
        validateIsAdmin(user);

        Item item = itemRepository.findByIdOrElseThrow(id);

        item.update(requestDto.getName(), requestDto.getDescription(), requestDto.getPrice());

        return ItemUpdateResponseDto.from(item);
    }

    @Override
    public ItemDetailResponseDto getById(Long id, User user) {
        validateIsAdmin(user);
        Item item = itemRepository.findByIdOrElseThrow(id);
        return ItemDetailResponseDto.from(item);
    }

    @Override
    public List<ItemResponseDto> getAll(User user) {
        validateIsAdmin(user);
        return itemRepository.findAll().stream()
                .map(ItemResponseDto::from)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void delete(Long id,User user) {
        validateIsAdmin(user);
        Item item = itemRepository.findByIdOrElseThrow(id);
        itemRepository.delete(item);
    }
    @Override
    public void checkAdmin(User user) {
        EntityValidator.validateIsAdmin(user);
    }
}