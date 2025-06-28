package com.example.teambackend.web.item.service;




import com.example.teambackend.web.item.dto.*;
import com.example.teambackend.web.user.domain.User;

import java.util.List;

public interface ItemService {
    ItemCreateResponseDto create(ItemCreateRequestDto requestDto, User user);
    ItemUpdateResponseDto update(Long id, ItemUpdateRequestDto requestDtoUser, User user);
    ItemDetailResponseDto getById(Long id, User user);
    List<ItemResponseDto> getAll(User user);
    void delete(Long id,User user);
    void checkAdmin(User user);


}