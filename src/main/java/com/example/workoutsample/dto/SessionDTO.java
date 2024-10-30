package com.example.workoutsample.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class SessionDTO {
    private Long id;

    @NotNull(message = "日付を選択してください")
    @PastOrPresent(message = "現在または過去の日付を入力してください")
    private LocalDate date;

    private List<ExerciseDTO> exercises; // ExerciseもDTOに変換

    private Long userId; // UserはIDだけを保持

    @NotNull(message = "部位を選択してください") // ボディパートが必須
    private BodyPartDTO bodyPart; // BodyPartもDTOに変換

    private boolean isDeleted;

    public SessionDTO() {
    }

    public SessionDTO(Long id, LocalDate date, List<ExerciseDTO> exercises, Long userId, BodyPartDTO bodyPart, boolean isDeleted) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
        this.userId = userId;
        this.bodyPart = bodyPart;
        this.isDeleted = isDeleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
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

    public BodyPartDTO getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(BodyPartDTO bodyPart) {
        this.bodyPart = bodyPart;
    }
}
