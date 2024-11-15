package com.example.workoutsample.dto;

import java.time.LocalDateTime;

/**
 * 操作ログ情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * 主にログに関する情報を保持します。
 */
public class OperationLogDTO {

    /**
     * 操作ログの一意のID。
     */
    private Long id;

    /**
     * 操作を実行したユーザーのユーザー名。
     */
    private String username;

    /**
     * 実行された操作内容（例: "CREATE", "UPDATE", "DELETE"など）。
     */
    private String action;

    /**
     * 操作が実行された日時。
     */
    private LocalDateTime timestamp;

    /**
     * デフォルトコンストラクタ。
     */
    public OperationLogDTO() {
    }

    /**
     * フィールドを指定してOperationLogDTOを作成します。
     *
     * @param id 操作ログの一意のID
     * @param username 操作を実行したユーザー名
     * @param action 実行された操作内容
     * @param timestamp 操作が実行された日時
     */
    public OperationLogDTO(Long id, String username, String action, LocalDateTime timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    /**
     * 操作ログのIDを取得します。
     *
     * @return 操作ログのID
     */
    public Long getId() {
        return id;
    }

    /**
     * 操作ログのIDを設定します。
     *
     * @param id 操作ログのID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 操作を実行したユーザー名を取得します。
     *
     * @return 操作を実行したユーザー名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 操作を実行したユーザー名を設定します。
     *
     * @param username 操作を実行したユーザー名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 実行された操作内容を取得します。
     *
     * @return 実行された操作内容
     */
    public String getAction() {
        return action;
    }

    /**
     * 実行された操作内容を設定します。
     *
     * @param action 実行された操作内容
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 操作が実行された日時を取得します。
     *
     * @return 操作が実行された日時
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 操作が実行された日時を設定します。
     *
     * @param timestamp 操作が実行された日時
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
