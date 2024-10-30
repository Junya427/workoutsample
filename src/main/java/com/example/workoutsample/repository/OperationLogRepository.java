package com.example.workoutsample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.OperationLog;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
}