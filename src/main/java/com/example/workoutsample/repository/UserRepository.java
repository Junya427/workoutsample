package com.example.workoutsample.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workoutsample.model.User;

/**
 * {@link User}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 指定されたユーザーネームに一致するユーザーを検索します。
     *
     * @param username ユーザーネーム
     * @return ユーザーネームに一致するユーザー
     */
    User findByUsername(String username);

    /**
     * 指定された部分一致するユーザーネームと権限IDに基づいてユーザーを検索します。
     *
     * @param username ユーザーネームの部分一致検索条件
     * @param authorityId 権限ID
     * @return 条件に一致するユーザーのリスト
     */
    List<User> findByUsernameContainingAndAuthorities_Id(String username, Long authorityId);

    /**
     * 指定された部分一致するユーザーネームに基づいてユーザーを検索します。
     *
     * @param username ユーザーネームの部分一致検索条件
     * @return 条件に一致するユーザーのリスト
     */
    List<User> findByUsernameContaining(String username);

    /**
     * 指定された権限IDに基づいてユーザーを検索します。
     *
     * @param authorityId 権限ID
     * @return 権限IDに一致するユーザーのリスト
     */
    List<User> findByAuthorities_Id(Long authorityId);

    /**
     * 指定されたユーザーIDに一致するユーザーを検索します。
     *
     * @param userId ユーザーID
     * @return ユーザーIDに一致するユーザーを含むOptionalオブジェクト
     */
    Optional<User> findUserById(Long userId);
}
