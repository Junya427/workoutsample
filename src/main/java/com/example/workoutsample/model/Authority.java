package com.example.workoutsample.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * ユーザーの権限を表すエンティティクラスです。
 * 権限情報は複数のユーザーと関連付けられる場合があります。
 */
@Entity
@Table(name = "authorities")
public class Authority {

    /**
     * 権限の一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 権限名（例: ROLE_ADMIN, ROLE_USER）。
     */
    @Column
    private String authority;

    /**
     * この権限を持つユーザーのセット。
     * 多対多の関係で、Userエンティティにマッピングされています。
     */
    @ManyToMany(mappedBy = "authorities")
    private Set<User> user;

    /**
     * デフォルトコンストラクタ。
     */
    public Authority() {
    }

    /**
     * フィールドを指定してAuthorityオブジェクトを作成します。
     *
     * @param id 権限ID
     * @param authority 権限名
     * @param user この権限を持つユーザーのセット
     */
    public Authority(Long id, String authority, Set<User> user) {
        this.id = id;
        this.authority = authority;
        this.user = user;
    }

    /**
     * 権限IDを取得します。
     *
     * @return 権限ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 権限IDを設定します。
     *
     * @param id 権限ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 権限名を取得します。
     *
     * @return 権限名
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * 権限名を設定します。
     *
     * @param authority 権限名
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    /**
     * この権限を持つユーザーのセットを取得します。
     *
     * @return ユーザーのセット
     */
    public Set<User> getUser() {
        return user;
    }

    /**
     * この権限を持つユーザーのセットを設定します。
     *
     * @param user ユーザーのセット
     */
    public void setUser(Set<User> user) {
        this.user = user;
    }
}
