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

@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "エクササイズ名を入力してください")
    private String name;

    @NotNull(message = "重量を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(400)
    @Column(nullable = false)
    private Long weight;

    @NotNull(message = "挙上回数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(100)
    @Column(nullable = false)
    private Long reps;

    @NotNull(message = "セット数を入力してください")
    @Positive(message = "正の値を入力してください")
    @Max(30)
    @Column(nullable = false)
    private Long sets;

    @NotNull
    @Positive
    @Column(nullable = false)
    private Long volume;

    private String weightStatus = "";

    private String repStatus = "";

    @ManyToOne//多対１の設定。１対多ならOneToMany。書籍たちはそれぞれ一つのジャンルを持っている
    @JoinColumn(name = "session_id", nullable = false)//Bookエンティティにはgenre_idを持たせずにGenre自体を持たせたが、DBのテーブルにはgenre_idが必要。
        //Bookのgenre_idとGenreのidでJOINせよという命令
    private Session session;//genre_idではなくGenreエンティティ自体をBookに持たせる

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private boolean isDeleted = false;

    
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
        this.isDeleted = isDeleted; // 追加
        updateVolume();
    }
    

    
    public Exercise() {
    }


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
        updateVolume();
    }
    public Long getReps() {
        return reps;
    }
    public void setReps(Long reps) {
        this.reps = reps;
        updateVolume();
    }
    public Long getSets() {
        return sets;
    }
    public void setSets(Long sets) {
        this.sets = sets;
        updateVolume();
    }

    public Long getVolume() {
        return volume;
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDelete(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    
    private void updateVolume() {
        if (weight != null && reps != null && sets != null) {
            this.volume = weight * reps * sets;
        } else {
            this.volume = 0L;
        }
    }


}
