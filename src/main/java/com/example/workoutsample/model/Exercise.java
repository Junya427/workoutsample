package com.example.workoutsample.model;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * トレーニングのエクササイズ情報を表すエンティティクラスです。
 * 各エクササイズはユーザーおよびセッションに関連付けられています。
 */
@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "exercises")
public class Exercise {

    /**
     * エクササイズの一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * エクササイズ名。
     * 必須項目で空白ではいけません。
     */
    @NotBlank(message = "エクササイズ名を入力してください")
    private String name;

    /**
     * 使用重量（kg）。
     * 必須項目で正の値、最大400まで許容されます。
     */
    @NotNull(message = "重量を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(400)
    @Column(nullable = false)
    private Long weight;

    /**
     * 挙上回数。
     * 必須項目で正の値、最大100まで許容されます。
     */
    @NotNull(message = "挙上回数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(100)
    @Column(nullable = false)
    private Long reps;

    /**
     * セット数。
     * 必須項目で正の値、最大30まで許容されます。
     */
    @NotNull(message = "セット数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(30)
    @Column(nullable = false)
    private Long sets;

    /**
     * ボリューム（重量 × 挙上回数 × セット数）。
     */
    @NotNull
    @Positive
    @Column(nullable = false)
    private Long volume;

    /**
     * 使用重量の増減状況を表す状態。
     */
    private String weightStatus = "";

    /**
     * 挙上回数の増減状況を表す状態。
     */
    private String repStatus = "";

    /**
     * このエクササイズが属するセッション。
     */
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    /**
     * このエクササイズを登録したユーザー。
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 削除フラグ。
     * trueの場合、このエクササイズは論理的に削除されています。
     */
    @Column
    private boolean isDeleted = false;

    /**
     * デフォルトコンストラクタ。
     */
    public Exercise() {
    }

    /**
     * フィールドを指定してExerciseオブジェクトを作成します。
     *
     * @param id エクササイズID
     * @param name エクササイズ名
     * @param weight 使用重量
     * @param reps 挙上回数
     * @param sets セット数
     * @param weightStatus 使用重量の状態
     * @param repStatus 挙上回数の状態
     * @param session 所属するセッション
     * @param user 登録したユーザー
     * @param isDeleted 削除フラグ
     */
    public Exercise(Long id, String name, Long weight, Long reps, Long sets, String weightStatus, String repStatus, Session session, User user, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
        this.weightStatus = weightStatus;
        this.repStatus = repStatus;
        this.session = session;
        this.user = user;
        this.isDeleted = isDeleted;
        updateVolume();
    }


    /**
     * エクササイズのIDを取得します。
     * 
     * @return エクササイズID
     */
    public Long getId() {
        return id;
    }

    /**
     * エクササイズのIDを設定します。
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
     * 使用重量を取得します。
     * 
     * @return 使用重量
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * 使用重量を設定します。
     * 
     * @param weight 使用重量
     */
    public void setWeight(Long weight) {
        this.weight = weight;
        updateVolume();
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
        updateVolume();
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
        updateVolume();
    }

    /**
     * ボリュームを取得します。
     * 
     * @return ボリューム（重量 × 挙上回数 × セット数）
     */
    public Long getVolume() {
        return volume;
    }

    /**
     * 使用重量の状態を取得します。
     * 
     * @return 使用重量の状態
     */
    public String getWeightStatus() {
        return weightStatus;
    }

    /**
     * 使用重量の状態を設定します。
     * 
     * @param weightStatus 使用重量の状態
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
     * セッションを取得します。
     * 
     * @return セッション
     */
    public Session getSession() {
        return session;
    }

    /**
     * セッションを設定します。
     * 
     * @param session セッション
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * 登録ユーザーを取得します。
     * 
     * @return 登録ユーザー
     */
    public User getUser() {
        return user;
    }

    /**
     * 登録ユーザーを設定します。
     * 
     * @param user 登録ユーザー
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 削除フラグを取得します。
     * 
     * @return trueの場合、論理的に削除されています。
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * 削除フラグを設定します。
     * 
     * @param isDeleted trueの場合、このエクササイズは削除されています。
     */
    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    /**
     * ボリュームを更新するための内部メソッド。
     * 重量、回数、セット数がすべて設定されている場合に計算されます。
     */
    private void updateVolume() {
        if (weight != null && reps != null && sets != null) {
            this.volume = weight * reps * sets;
        } else {
            this.volume = 0L;
        }
    }


}
