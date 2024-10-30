package com.example.workoutsample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workoutsample.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    List<User> findByUsernameAndAuthorities_Id(String username, Long authorityId);

    // ユーザーネームの部分一致で検索
    List<User> findByUsernameContaining(String username);

    // 権限IDで検索
    List<User> findByAuthorities_Id(Long authorityId);

    Optional<User> findUserById(Long userId);
}