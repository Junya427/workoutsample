package com.example.workoutsample.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * トレーニングセッション情報を格納するためのデータ転送オブジェクト（DTO）クラスです。
 * セッションの日付、エクササイズ、ユーザー情報などを保持します。
 */
public class SessionDTO {

    /**
     * セッションの一意のID。
     */
    private Long id;

    /**
     * セッションの日付。
     * 必須入力で、現在または過去の日付を指定する必要があります。
     */
    @NotNull(message = "日付を選択してください")
    @PastOrPresent(message = "現在または過去の日付を入力してください")
    private LocalDate date;

    /**
     * セッションに含まれるエクササイズのリスト。
     */
    private List<ExerciseDTO> exercises;

    /**
     * セッションを登録したユーザーのID。
     */
    private Long userId;

    /**
     * セッションに関連付けられた部位情報。
     * 必須項目です。
     */
    @NotNull(message = "部位を選択してください")
    private BodyPartDTO bodyPart;

    /**
     * 削除フラグ。
     * セッションが削除されているかどうかを示します。
     */
    private boolean isDeleted;

    /**
     * デフォルトコンストラクタ。
     */
    public SessionDTO() {
    }

    /**
     * フィールドを指定してSessionDTOを作成します。
     *
     * @param id セッションID
     * @param date セッションの日付
     * @param exercises セッションに含まれるエクササイズのリスト
     * @param userId セッションを登録したユーザーのID
     * @param bodyPart セッションに関連付けられた部位情報
     * @param isDeleted 削除フラグ
     */
    public SessionDTO(Long id, LocalDate date, List<ExerciseDTO> exercises, Long userId, BodyPartDTO bodyPart, boolean isDeleted) {
        this.id = id;
        this.date = date;
        this.exercises = exercises;
        this.userId = userId;
        this.bodyPart = bodyPart;
        this.isDeleted = isDeleted;
    }

    /**
     * セッションIDを取得します。
     *
     * @return セッションID
     */
    public Long getId() {
        return id;
    }

    /**
     * セッションIDを設定します。
     *
     * @param id セッションID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * セッションの日付を取得します。
     *
     * @return セッションの日付
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * セッションの日付を設定します。
     *
     * @param date セッションの日付
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * セッションに含まれるエクササイズのリストを取得します。
     *
     * @return セッションに含まれるエクササイズのリスト
     */
    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    /**
     * セッションに含まれるエクササイズのリストを設定します。
     *
     * @param exercises セッションに含まれるエクササイズのリスト
     */
    public void setExercises(List<ExerciseDTO> exercises) {
        this.exercises = exercises;
    }

    /**
     * セッションを登録したユーザーのIDを取得します。
     *
     * @return セッションを登録したユーザーのID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * セッションを登録したユーザーのIDを設定します。
     *
     * @param userId セッションを登録したユーザーのID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * セッションに関連付けられた部位情報を取得します。
     *
     * @return セッションに関連付けられた部位情報
     */
    public BodyPartDTO getBodyPart() {
        return bodyPart;
    }

    /**
     * セッションに関連付けられた部位情報を設定します。
     *
     * @param bodyPart セッションに関連付けられた部位情報
     */
    public void setBodyPart(BodyPartDTO bodyPart) {
        this.bodyPart = bodyPart;
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
