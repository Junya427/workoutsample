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

@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // アカウント
    @NotBlank(message="ユーザーネームを入力してください")
    @Email(message = "e-mail形式で入力してください")
    @Column(nullable = false, unique = true)
    private String username;
    
    // パスワード
    @NotBlank(message = "パスワードを入力してください")
    @Column(nullable = false)
    private String password;

    // 有効・無効フラグ
    @Column(nullable = false)
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_authority",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "user")
    private Set<Exercise> exercises;

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public User() {
    }

    public User(Long id, String username, String password, boolean enabled, Set<Authority> authorities, Set<Exercise> exercises, Set<Session> sessions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
        this.exercises = exercises;
        this.sessions = sessions;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
