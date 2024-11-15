package com.example.workoutsample.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workoutsample.dto.OperationLogDTO;
import com.example.workoutsample.mapper.OperationLogMapper;
import com.example.workoutsample.model.OperationLog;
import com.example.workoutsample.repository.OperationLogRepository;

/**
 * 操作ログに関するビジネスロジックを提供するサービスクラスです。
 * 操作ログの記録、取得、条件検索の機能をサポートします。
 */
@Service
public class OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private OperationLogMapper operationLogMapper;

    /**
     * 操作ログを記録します。
     *
     * @param username 操作を行ったユーザー名
     * @param action 実行された操作内容
     */
    public void logOperation(String username, String action) {
        OperationLog operationLog = new OperationLog(username, action, LocalDateTime.now());
        operationLogRepository.save(operationLog);
    }

    /**
     * すべての操作ログを取得します。
     *
     * @return {@link OperationLogDTO}のリスト
     */
    public List<OperationLogDTO> findAllLogs() {
        List<OperationLog> operationLogs = operationLogRepository.findAll();
        return operationLogMapper.toDTOList(operationLogs);
    }

    /**
     * 条件に基づいて操作ログを検索します。
     *
     * @param username ユーザー名（部分一致）
     * @param action 操作内容（部分一致）
     * @param startDate 検索範囲の開始日時
     * @param endDate 検索範囲の終了日時
     * @return {@link OperationLogDTO}のリスト
     */
    public List<OperationLogDTO> searchLogs(String username, String action, LocalDateTime startDate, LocalDateTime endDate) {
        return operationLogRepository.findAll().stream()
            .filter(log -> (username == null || username.isEmpty() || log.getUsername().contains(username)))
            .filter(log -> (action == null || action.isEmpty() || log.getAction().contains(action)))
            .filter(log -> (startDate == null || endDate == null ||
                            (!log.getTimestamp().isBefore(startDate) && !log.getTimestamp().isAfter(endDate))))
            .map(operationLogMapper::toDTO)
            .toList();
    }
}
