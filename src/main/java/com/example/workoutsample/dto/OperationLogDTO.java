package com.example.workoutsample.dto;

import java.time.LocalDateTime;

public class OperationLogDTO {
    private Long id;
    private String username;
    private String action;
    private LocalDateTime timestamp;

    // コンストラクタ
    public OperationLogDTO() {
    }

    public OperationLogDTO(Long id, String username, String action, LocalDateTime timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}