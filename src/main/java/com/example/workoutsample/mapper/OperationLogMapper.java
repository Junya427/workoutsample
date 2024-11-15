package com.example.workoutsample.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.OperationLogDTO;
import com.example.workoutsample.model.OperationLog;

/**
 * OperationLogエンティティとOperationLogDTOの間の変換を行うマッパークラスです。
 * エンティティとDTO間のデータ変換ロジックを提供します。
 */
@Component
public class OperationLogMapper {

    /**
     * OperationLogエンティティをOperationLogDTOに変換します。
     *
     * @param operationLog OperationLogエンティティ
     * @return OperationLogDTOオブジェクト
     */
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

    /**
     * OperationLogエンティティのリストをOperationLogDTOのリストに変換します。
     *
     * @param operationLogs OperationLogエンティティのリスト
     * @return OperationLogDTOのリスト
     */
    public List<OperationLogDTO> toDTOList(List<OperationLog> operationLogs) {
        return operationLogs.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * OperationLogDTOをOperationLogエンティティに変換します。
     *
     * @param operationLogDTO OperationLogDTOオブジェクト
     * @return OperationLogエンティティ
     */
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
