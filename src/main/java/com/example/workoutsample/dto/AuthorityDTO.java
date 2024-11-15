package com.example.workoutsample.dto;

public class AuthorityDTO {
    private Long id;
    private String authority;

    // コンストラクタ
    public AuthorityDTO() {
    }

    public AuthorityDTO(Long id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "AuthorityDTO{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                '}';
    }
}
