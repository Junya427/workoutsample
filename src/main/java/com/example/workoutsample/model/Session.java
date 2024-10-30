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

    @Entity
    @SQLRestriction("is_deleted = false")
    @Table(name = "sessions")
    public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull(message = "日付を選択してください")
    @PastOrPresent(message = "現在または過去の日付を入力してください")
    private LocalDate date;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)//BookクラスのManyToOneで設定した変数genreをmappedByする。
    //cascadeは連なっているという意味。親が削除されたら紐づくこも削除される。
    private List<Exercise> exercises = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private boolean isDeleted = false;

    public Session(){
    }

    public Session(Long id, LocalDate date, BodyPart bodyPart, User user, List<Exercise> exercises) {
        this.id = id;
        this.date = date;
        this.bodyPart = bodyPart;
        this.user = user;
        this.exercises = exercises;
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

    public List<Exercise> getExercises() {
        return exercises;
    }
    
    public void setExercise(List<Exercise> exercises) {
        this.exercises = exercises;
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

    public BodyPart getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(BodyPart bodyPart) {
        this.bodyPart = bodyPart;
    }

    @ManyToOne//多対１の設定。１対多ならOneToMany。書籍たちはそれぞれ一つのジャンルを持っている
    @JoinColumn(name = "bodyPart_id", nullable = false)//Bookエンティティにはgenre_idを持たせずにGenre自体を持たせたが、DBのテーブルにはgenre_idが必要。
        //Bookのgenre_idとGenreのidでJOINせよという命令
    private BodyPart bodyPart;//genre_idではなくGenreエンティティ自体をBookに持たせる
}
