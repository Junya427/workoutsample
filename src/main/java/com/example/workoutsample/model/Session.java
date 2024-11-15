package com.example.workoutsample.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

/**
 * トレーニングセッションを表すエンティティクラスです。
 * 各セッションはユーザーおよびトレーニング部位に関連付けられています。
 */
@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "sessions")
public class Session {

    /**
     * セッションの一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * セッションの日付。
     * 必須項目で、現在または過去の日付のみが許可されます。
     */
    @Column(nullable = false)
    @NotNull(message = "日付を選択してください")
    @PastOrPresent(message = "現在または過去の日付を入力してください")
    private LocalDate date;

    /**
     * セッションに関連付けられたエクササイズのリスト。
     * 親セッションが削除されると、関連付けられたエクササイズも削除されます。
     */
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Exercise> exercises = new ArrayList<>();

    /**
     * このセッションを登録したユーザー。
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * セッションの対象となるトレーニング部位。
     */
    @ManyToOne
    @JoinColumn(name = "bodyPart_id", nullable = false)
    private BodyPart bodyPart;

    /**
     * 削除フラグ。
     * trueの場合、このセッションは論理的に削除されています。
     */
    @Column
    private boolean isDeleted = false;

    /**
     * デフォルトコンストラクタ。
     */
    public Session() {
    }

    /**
     * フィールドを指定してSessionオブジェクトを作成します。
     *
     * @param id セッションID
     * @param date セッションの日付
     * @param bodyPart セッションのトレーニング部位
     * @param user セッションを登録したユーザー
     * @param exercises セッションに関連付けられたエクササイズのリスト
     */
    public Session(Long id, LocalDate date, BodyPart bodyPart, User user, List<Exercise> exercises) {
        this.id = id;
        this.date = date;
        this.bodyPart = bodyPart;
        this.user = user;
        this.exercises = exercises;
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
     * セッションに関連付けられたエクササイズのリストを取得します。
     *
     * @return エクササイズのリスト
     */
    public List<Exercise> getExercises() {
        return exercises;
    }

    /**
     * セッションに関連付けられたエクササイズのリストを設定します。
     *
     * @param exercises エクササイズのリスト
     */
    public void setExercise(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    /**
     * セッションを登録したユーザーを取得します。
     *
     * @return 登録ユーザー
     */
    public User getUser() {
        return user;
    }

    /**
     * セッションを登録したユーザーを設定します。
     *
     * @param user 登録ユーザー
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * セッションのトレーニング部位を取得します。
     *
     * @return トレーニング部位
     */
    public BodyPart getBodyPart() {
        return bodyPart;
    }

    /**
     * セッションのトレーニング部位を設定します。
     *
     * @param bodyPart トレーニング部位
     */
    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    /**
     * セッションの削除フラグを取得します。
     *
     * @return trueの場合、セッションは論理的に削除されています。
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * セッションの削除フラグを設定します。
     *
     * @param isDeleted trueの場合、このセッションは削除されています。
     */
    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
