package com.example.workoutsample.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workoutsample.dto.OperationLogDTO;
import com.example.workoutsample.mapper.OperationLogMapper;
import com.example.workoutsample.model.OperationLog;
import com.example.workoutsample.repository.OperationLogRepository;

@Service
public class OperationLogService {
    @Autowired
    private OperationLogRepository operationLogRepository;

    @Autowired
    private OperationLogMapper operationLogMapper;

    public void logOperation(String username, String action) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUsername(username);
        operationLog.setAction(action);
        operationLog.setTimestamp(LocalDateTime.now());
        operationLogRepository.save(operationLog);
    }

    public List<OperationLogDTO> findAllLogs() {
        List<OperationLog> operationLogs = operationLogRepository.findAll();
        return operationLogMapper.toDTOList(operationLogs);
    }

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

