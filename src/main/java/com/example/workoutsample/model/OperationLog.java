package com.example.workoutsample.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 操作ログ情報を表すエンティティクラスです。
 * 各ログには操作を行ったユーザー名、アクション内容、タイムスタンプが含まれます。
 */
@Entity
@Table(name = "operation_logs")
public class OperationLog {

    /**
     * 操作ログの一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作を行ったユーザーの名前。
     */
    private String username;

    /**
     * 実行されたアクションの内容。
     */
    private String action;

    /**
     * 操作が行われた日時。
     */
    private LocalDateTime timestamp;

    /**
     * デフォルトコンストラクタ。
     */
    public OperationLog() {
    }

    /**
     * 全フィールドを指定してOperationLogオブジェクトを作成します。
     *
     * @param id 操作ログID
     * @param username 操作を行ったユーザー名
     * @param action 実行されたアクション
     * @param timestamp 操作が行われた日時
     */
    public OperationLog(Long id, String username, String action, LocalDateTime timestamp) {
        this.id = id;
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
    }

    /**
     * id以外のフィールドを指定してOperationLogオブジェクトを作成します。
     * @param username
     * @param action
     * @param timestamp
     */
    public OperationLog(String username, String action, LocalDateTime timestamp) {
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
     * 操作を行ったユーザー名を取得します。
     *
     * @return 操作を行ったユーザー名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 操作を行ったユーザー名を設定します。
     *
     * @param username 操作を行ったユーザー名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 実行されたアクションの内容を取得します。
     *
     * @return 実行されたアクションの内容
     */
    public String getAction() {
        return action;
    }

    /**
     * 実行されたアクションの内容を設定します。
     *
     * @param action 実行されたアクションの内容
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * 操作が行われた日時を取得します。
     *
     * @return 操作が行われた日時
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 操作が行われた日時を設定します。
     *
     * @param timestamp 操作が行われた日時
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
