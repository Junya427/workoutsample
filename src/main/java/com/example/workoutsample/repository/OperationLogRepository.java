package com.example.workoutsample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.OperationLog;

/**
 * {@link OperationLog}エンティティに対するデータアクセス操作を提供するリポジトリインターフェースです。
 * Spring Data JPAを使用して、自動的にCRUD操作が実装されます。
 */
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

}
