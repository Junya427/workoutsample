package com.example.workoutsample.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workoutsample.model.Authority;

/**
 * {@link Authority}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    /**
     * 指定されたユーザーIDに関連付けられた権限を検索します。
     *
     * @param userId ユーザーのID
     * @return ユーザーIDに関連付けられた権限を含むOptionalオブジェクト
     */
    Optional<Authority> findByUserId(Long userId);

    /**
     * 指定された権限名に基づいて権限を検索します。
     *
     * @param authority 権限名（例: ROLE_ADMIN, ROLE_USERなど）
     * @return 権限名に関連付けられた権限を含むOptionalオブジェクト
     */
    Optional<Authority> findByAuthority(String authority);

}
