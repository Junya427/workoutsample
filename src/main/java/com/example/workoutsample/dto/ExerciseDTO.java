package com.example.workoutsample.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * エクササイズ情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * 主にエクササイズの詳細情報を保持します。
 */
public class ExerciseDTO {

    /**
     * エクササイズID
     */
    private Long id;

    /**
     * エクササイズ名
     * 必須入力項目です。
     */
    @NotBlank(message = "エクササイズ名を入力してください")
    private String name;

    /**
     * 重量
     * 必須入力で、正の値を入力する必要があります。
     */
    @NotNull(message = "重量を入力してください")
    @Positive(message = "正の値を入力してください")
    private Long weight;

    /**
     * 挙上回数
     * 必須入力で、最大100回までの正の値が許容されます。
     */
    @NotNull(message = "挙上回数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(100)
    @Column(nullable = false)
    private Long reps;

    /**
     * セット数
     * 必須入力で、最大30セットまでの正の値が許容されます。
     */
    @NotNull(message = "セット数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(30)
    @Column(nullable = false)
    private Long sets;

    /**
     * ボリューム（重量 × 挙上回数 × セット数）
     */
    private Long volume;

    /**
     * 重量の状態（例: 過去データとの比較結果）
     */
    private String weightStatus;

    /**
     * 挙上回数の状態（例: 過去データとの比較結果）
     */
    private String repStatus;

    /**
     * セッションID
     * エクササイズが属するトレーニングセッションを特定します。
     */
    private Long sessionId;

    /**
     * ユーザーID
     * エクササイズを登録したユーザーを特定します。
     */
    private Long userId;

    /**
     * 削除フラグ
     * エクササイズが削除されているかどうかを示します。
     */
    private boolean isDeleted;

    /**
     * デフォルトコンストラクタ。
     */
    public ExerciseDTO() {
    }

    /**
     * フィールドを指定してExerciseDTOを作成します。
     *
     * @param id エクササイズID
     * @param name エクササイズ名
     * @param weight 重量
     * @param reps 挙上回数
     * @param sets セット数
     * @param volume ボリューム（重量 × 挙上回数 × セット数）
     * @param weightStatus 重量の状態
     * @param repStatus 挙上回数の状態
     * @param sessionId セッションID
     * @param userId ユーザーID
     * @param isDeleted 削除フラグ
     */
    public ExerciseDTO(Long id, String name, Long weight, Long reps, Long sets, Long volume,
                    String weightStatus, String repStatus, Long sessionId, Long userId, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.volume = volume;
        this.weightStatus = weightStatus;
        this.repStatus = repStatus;
        this.sessionId = sessionId;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    /**
     * エクササイズIDを取得します。
     *
     * @return エクササイズID
     */
    public Long getId() {
        return id;
    }

    /**
     * エクササイズIDを設定します。
     *
     * @param id エクササイズID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * エクササイズ名を取得します。
     *
     * @return エクササイズ名
     */
    public String getName() {
        return name;
    }

    /**
     * エクササイズ名を設定します。
     *
     * @param name エクササイズ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 重量を取得します。
     *
     * @return 重量
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * 重量を設定します。
     *
     * @param weight 重量
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * 挙上回数を取得します。
     *
     * @return 挙上回数
     */
    public Long getReps() {
        return reps;
    }

    /**
     * 挙上回数を設定します。
     *
     * @param reps 挙上回数
     */
    public void setReps(Long reps) {
        this.reps = reps;
    }

    /**
     * セット数を取得します。
     *
     * @return セット数
     */
    public Long getSets() {
        return sets;
    }

    /**
     * セット数を設定します。
     *
     * @param sets セット数
     */
    public void setSets(Long sets) {
        this.sets = sets;
    }

    /**
     * ボリューム（重量 × 挙上回数 × セット数）を取得します。
     *
     * @return ボリューム
     */
    public Long getVolume() {
        return volume;
    }

    /**
     * ボリュームを設定します。
     *
     * @param volume ボリューム
     */
    public void setVolume(Long volume) {
        this.volume = volume;
    }

    /**
     * 重量の状態を取得します。
     *
     * @return 重量の状態
     */
    public String getWeightStatus() {
        return weightStatus;
    }

    /**
     * 重量の状態を設定します。
     *
     * @param weightStatus 重量の状態
     */
    public void setWeightStatus(String weightStatus) {
        this.weightStatus = weightStatus;
    }

    /**
     * 挙上回数の状態を取得します。
     *
     * @return 挙上回数の状態
     */
    public String getRepStatus() {
        return repStatus;
    }

    /**
     * 挙上回数の状態を設定します。
     *
     * @param repStatus 挙上回数の状態
     */
    public void setRepStatus(String repStatus) {
        this.repStatus = repStatus;
    }

    /**
     * セッションIDを取得します。
     *
     * @return セッションID
     */
    public Long getSessionId() {
        return sessionId;
    }

    /**
     * セッションIDを設定します。
     *
     * @param sessionId セッションID
     */
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * ユーザーIDを取得します。
     *
     * @return ユーザーID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * ユーザーIDを設定します。
     *
     * @param userId ユーザーID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 削除フラグを取得します。
     *
     * @return 削除フラグ
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * 削除フラグを設定します。
     *
     * @param isDeleted 削除フラグ
     */
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
