package com.example.workoutsample.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ExerciseDTO {
    private Long id;                // エクササイズのID

    @NotBlank(message = "エクササイズ名を入力してください")
    private String name;            // エクササイズ名

    @NotNull(message = "重量を入力してください")
    @Positive(message = "正の値を入力してください")
    private Long weight;            // 重量

    @NotNull(message = "挙上回数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(100)
    @Column(nullable = false)
    private Long reps;              // 挙上回数

    @NotNull(message = "セット数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(30)
    @Column(nullable = false)
    private Long sets;              // セット数

    private Long volume;            // ボリューム
    private String weightStatus;    // 重量の状態
    private String repStatus;       // 回数の状態
    private Long sessionId;         // セッションID
    private Long userId;            // ユーザーID
    private boolean isDeleted;      // 削除フラグ

    // コンストラクタ
    public ExerciseDTO() {
    }

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

    // ゲッターとセッター
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getReps() {
        return reps;
    }

    public void setReps(Long reps) {
        this.reps = reps;
    }

    public Long getSets() {
        return sets;
    }

    public void setSets(Long sets) {
        this.sets = sets;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public String getWeightStatus() {
        return weightStatus;
    }

    public void setWeightStatus(String weightStatus) {
        this.weightStatus = weightStatus;
    }

    public String getRepStatus() {
        return repStatus;
    }

    public void setRepStatus(String repStatus) {
        this.repStatus = repStatus;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
