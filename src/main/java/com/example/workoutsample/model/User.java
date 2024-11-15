package com.example.workoutsample.model;

import java.util.Set;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * ユーザー情報を表すエンティティクラスです。
 * 各ユーザーには権限、エクササイズ、およびセッションが関連付けられています。
 */
@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "users")
public class User {

    /**
     * ユーザーの一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ユーザー名（メールアドレス形式）。
     * 必須項目で、ユニークである必要があります。
     */
    @NotBlank(message = "ユーザーネームを入力してください")
    @Email(message = "e-mail形式で入力してください")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * ユーザーのパスワード。
     * 必須項目です。
     */
    @NotBlank(message = "パスワードを入力してください")
    @Column(nullable = false)
    private String password;

    /**
     * ユーザーが有効かどうかを示すフラグ。
     */
    @Column(nullable = false)
    private boolean enabled;

    /**
     * ユーザーに関連付けられた権限のセット。
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    /**
     * ユーザーが登録したエクササイズのセット。
     */
    @OneToMany(mappedBy = "user")
    private Set<Exercise> exercises;

    /**
     * ユーザーが登録したセッションのセット。
     */
    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

    /**
     * ユーザーの削除フラグ。
     * trueの場合、ユーザーは論理的に削除されています。
     */
    @Column(nullable = false)
    private boolean isDeleted = false;

    /**
     * デフォルトコンストラクタ。
     */
    public User() {
    }

    /**
     * フィールドを指定してUserオブジェクトを作成します。
     *
     * @param id ユーザーID
     * @param username ユーザー名（メールアドレス形式）
     * @param password ユーザーのパスワード
     * @param enabled ユーザーの有効・無効フラグ
     * @param authorities ユーザーに関連付けられた権限
     * @param exercises ユーザーが登録したエクササイズ
     * @param sessions ユーザーが登録したセッション
     */
    public User(Long id, String username, String password, boolean enabled, Set<Authority> authorities, Set<Exercise> exercises, Set<Session> sessions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
        this.exercises = exercises;
        this.sessions = sessions;
    }

    /**
     * ユーザーIDを取得します。
     *
     * @return ユーザーID
     */
    public Long getId() {
        return id;
    }

    /**
     * ユーザーIDを設定します。
     *
     * @param id ユーザーID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * ユーザー名を取得します。
     *
     * @return ユーザー名
     */
    public String getUsername() {
        return username;
    }

    /**
     * ユーザー名を設定します。
     *
     * @param username ユーザー名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * ユーザーのパスワードを取得します。
     *
     * @return ユーザーのパスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * ユーザーのパスワードを設定します。
     *
     * @param password ユーザーのパスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * ユーザーが有効かどうかを取得します。
     *
     * @return trueの場合、ユーザーは有効
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * ユーザーが有効かどうかを設定します。
     *
     * @param enabled trueの場合、ユーザーを有効に設定
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * ユーザーに関連付けられた権限を取得します。
     *
     * @return 権限のセット
     */
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * ユーザーに関連付けられた権限を設定します。
     *
     * @param authorities 権限のセット
     */
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     * ユーザーが登録したエクササイズを取得します。
     *
     * @return エクササイズのセット
     */
    public Set<Exercise> getExercises() {
        return exercises;
    }

    /**
     * ユーザーが登録したエクササイズを設定します。
     *
     * @param exercises エクササイズのセット
     */
    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
     * ユーザーが登録したセッションを取得します。
     *
     * @return セッションのセット
     */
    public Set<Session> getSessions() {
        return sessions;
    }

    /**
     * ユーザーが登録したセッションを設定します。
     *
     * @param sessions セッションのセット
     */
    public void setSessions(Set<Session> sessions) {
        this.sessions = sessions;
    }

    /**
     * ユーザーの削除フラグを取得します。
     *
     * @return trueの場合、ユーザーは削除されています
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * ユーザーの削除フラグを設定します。
     *
     * @param isDeleted trueの場合、ユーザーを削除された状態に設定
     */
    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
