package com.example.workoutsample.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workoutsample.model.OperationLog;
import com.example.workoutsample.repository.OperationLogRepository;

@Service
public class OperationLogService {
    
    @Autowired
    private OperationLogRepository operationLogRepository;

    public void logOperation(String username, String action) {
        OperationLog operationLog = new OperationLog();
        operationLog.setUsername(username);
        operationLog.setAction(action);
        operationLog.setTimestamp(LocalDateTime.now());
        operationLogRepository.save(operationLog);
    }

    public List<OperationLog> findAllLogs() {
        return operationLogRepository.findAll();
    }
}
