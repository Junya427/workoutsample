package com.example.workoutsample.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.OperationLogDTO;
import com.example.workoutsample.model.OperationLog;

@Component
public class OperationLogMapper {

    public OperationLogDTO toDTO(OperationLog operationLog) {
        if (operationLog == null) {
            return null;
        }
        return new OperationLogDTO(
            operationLog.getId(),
            operationLog.getUsername(),
            operationLog.getAction(),
            operationLog.getTimestamp()
        );
    }

    public List<OperationLogDTO> toDTOList(List<OperationLog> operationLogs) {
        return operationLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OperationLog toEntity(OperationLogDTO operationLogDTO) {
        if (operationLogDTO == null) {
            return null;
        }
        OperationLog operationLog = new OperationLog();
        operationLog.setId(operationLogDTO.getId());
        operationLog.setUsername(operationLogDTO.getUsername());
        operationLog.setAction(operationLogDTO.getAction());
        operationLog.setTimestamp(operationLogDTO.getTimestamp());
        return operationLog;
    }
}

