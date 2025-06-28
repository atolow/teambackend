package com.example.teambackend.web.item.repository;



import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.item.domain.Item;
import com.example.teambackend.web.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 특정 유저가 등록한 아이템들 (운영자 확인용)
    List<Item> findByUser(User user);

    // 판매되지 않은 아이템만 조회    // 특정 유저가 등록한 아이템들 (운영자 확인용)
    //    List<Item> findByUser(User user);
    //
    //    // 판매되지 않은 아이템만 조회
    //    List<Item> findByIsSoldFalse();
    //
    //    // 이름으로 검색 (선택)
    //    List<Item> findByNameContaining(String keyword);
    //    Optional<Item> findByName(String name);
    //
    //    Optional<Item> findByItem(String item);
    //
    //    default Item findByItemOrElseThrow(String item) {
    //        return findByItem(item).orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));
    //    }
    //
    //    default Item findByIdOrElseThrow(Long id) {
    //        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 아이템이 존재하지 않습니다."));
    //    }
    List<Item> findByIsSoldFalse();

    // 이름으로 검색 (선택)
    List<Item> findByNameContaining(String keyword);

    Optional<Item> findByName(String name);

    default Item findByItemOrElseThrow(String name) {
        return findByName(name).orElseThrow(() -> new InvalidCredentialsException("해당 아이템이 존재하지 않습니다."));
    }

    default Item findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("해당 아이템이 존재하지 않습니다."));
    }

}