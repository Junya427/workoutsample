package com.example.workoutsample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workoutsample.model.BodyPart;

/**
 * {@link BodyPart}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
@Repository
public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {

}
