package com.example.workoutsample.dto;

import jakarta.validation.constraints.NotNull;

/**
 * トレーニング部位情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * 主に部位に関する情報を保持します。
 */
public class BodyPartDTO {

    /**
     * 部位ID。必須項目です。
     */
    @NotNull(message = "部位を選択してください")
    private Long id;

    /**
     * 部位名。
     */
    private String name;

    /**
     * デフォルトコンストラクタ。
     */
    public BodyPartDTO() {
    }

    /**
     * フィールドを指定してBodyPartDTOを作成します。
     *
     * @param id 部位ID
     * @param name 部位名
     */
    public BodyPartDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * 部位IDを取得します。
     *
     * @return 部位ID
     */
    public Long getId() {
        return id;
    }

    /**
     * 部位IDを設定します。
     *
     * @param id 部位ID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 部位名を取得します。
     *
     * @return 部位名
     */
    public String getName() {
        return name;
    }

    /**
     * 部位名を設定します。
     *
     * @param name 部位名
     */
    public void setName(String name) {
        this.name = name;
    }
}
