package com.example.teambackend.web.user.repository;

import com.example.teambackend.common.global.exception.InvalidCredentialsException;
import com.example.teambackend.web.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    default User findByUserNameOrElseThrow(String username) {
        return findByUsername(username).orElseThrow(() -> new InvalidCredentialsException("사용자를 찾을 수 없습니다."));
    }

    default User findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new InvalidCredentialsException("사용자를 찾을 수 없습니다."));
    }

    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT o.balance FROM CMI_users o WHERE o.username = :username", nativeQuery = true)
    Double findBalanceByUsername(@Param("username") String username);


    @Query(value = "SELECT c.username, c.balance " +
            "FROM CMI_users c " +
            "LEFT JOIN users u ON c.username = u.username " +
            "WHERE c.username NOT LIKE 'town%' " +
            "AND c.balance > 0 " +
            "ORDER BY c.balance DESC", nativeQuery = true)
    List<Object[]> findBalanceFromCMIUsers();
}
