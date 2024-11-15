package com.example.workoutsample.dto;

/**
 * 権限情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * 主にユーザーの権限に関する情報を保持します。
 */
public class AuthorityDTO {

    private Long id; // 権限ID
    private String authority; // 権限名

    /**
     * デフォルトコンストラクタ。
     */
    public AuthorityDTO() {
    }

    /**
     * フィールドを指定してAuthorityDTOを作成します。
     *
     * @param id 権限ID
     * @param authority 権限名
     */
    public AuthorityDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
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
}
