package com.example.workoutsample.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * ユーザー情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * ユーザー名、パスワード、権限情報などを保持します。
 */
public class UserDTO {

    /**
     * ユーザーの一意のID。
     */
    private Long id;

    /**
     * ユーザー名（e-mail形式）。
     * 必須入力項目であり、e-mail形式で入力する必要があります。
     */
    @NotBlank(message = "ユーザーネームを入力してください")
    @Email(message = "e-mail形式で入力してください")
    private String username;

    /**
     * ユーザーのパスワード。
     * 必須入力項目です。
     */
    @NotBlank(message = "パスワードを入力してください")
    private String password;

    /**
     * ユーザーの有効状態。
     * trueの場合、有効なユーザーとして扱われます。
     */
    private boolean enabled;

    /**
     * ユーザーに割り当てられた権限のセット。
     */
    private Set<AuthorityDTO> authorities;

    /**
     * デフォルトコンストラクタ。
     */
    public UserDTO() {
    }

    /**
     * フィールドを指定してUserDTOを作成します。
     *
     * @param id ユーザーID
     * @param username ユーザー名（e-mail形式）
     * @param password ユーザーパスワード
     * @param enabled ユーザーの有効状態
     * @param authorities ユーザーに割り当てられた権限のセット
     */
    public UserDTO(Long id, String username, String password, boolean enabled, Set<AuthorityDTO> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
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
     * ユーザーパスワードを取得します。
     *
     * @return ユーザーパスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * ユーザーパスワードを設定します。
     *
     * @param password ユーザーパスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * ユーザーの有効状態を取得します。
     *
     * @return ユーザーの有効状態
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * ユーザーの有効状態を設定します。
     *
     * @param enabled ユーザーの有効状態
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * ユーザーに割り当てられた権限のセットを取得します。
     *
     * @return 権限のセット
     */
    public Set<AuthorityDTO> getAuthorities() {
        return authorities;
    }

    /**
     * ユーザーに割り当てられた権限のセットを設定します。
     *
     * @param authorities 権限のセット
     */
    public void setAuthorities(Set<AuthorityDTO> authorities) {
        this.authorities = authorities;
    }
}
