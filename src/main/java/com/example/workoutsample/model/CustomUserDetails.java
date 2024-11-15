package com.example.workoutsample.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Securityの認証に使用されるカスタムUserDetailsクラスです。
 * Spring Securityの{@link UserDetails}インターフェースを実装し、
 * カスタムUserエンティティと統合しています。
 */
public class CustomUserDetails implements UserDetails {

    /**
     * カスタムUserエンティティのインスタンス。
     */
    private User user;

    /**
     * カスタムUserエンティティを使用してCustomUserDetailsを作成します。
     *
     * @param user カスタムUserエンティティ
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * ユーザーの権限を取得します。
     * カスタムUserエンティティの{@code authorities}フィールドから
     * {@link SimpleGrantedAuthority}オブジェクトのリストを生成します。
     *
     * @return 権限のリスト
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
            .collect(Collectors.toList());
    }

    /**
     * ユーザーのパスワードを取得します。
     *
     * @return ユーザーのパスワード
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * ユーザー名を取得します。
     *
     * @return ユーザー名
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * アカウントが期限切れでないかどうかを示します。
     *
     * @return true（アカウントは常に有効）
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * アカウントがロックされていないかどうかを示します。
     *
     * @return true（アカウントは常にロックされていない）
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 資格情報（パスワード）が期限切れでないかどうかを示します。
     *
     * @return true（資格情報は常に有効）
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * ユーザーが有効かどうかを示します。
     * カスタムUserエンティティの{@code isEnabled()}メソッドを使用します。
     *
     * @return ユーザーが有効である場合はtrue
     */
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * カスタムUserエンティティを取得します。
     *
     * @return カスタムUserエンティティ
     */
    public User getUser() {
        return user;
    }
}
