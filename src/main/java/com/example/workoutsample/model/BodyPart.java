package com.example.workoutsample.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * トレーニングの部位情報を表すエンティティクラスです。
 * セッションと関連付けられています。
 */
@Entity
@Table(name = "bodyParts")
public class BodyPart {

    /**
     * トレーニング部位の一意な識別子。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * トレーニング部位の名前。
     * 必須項目です。
     */
    @Column(nullable = false)
    private String name;

    /**
     * このトレーニング部位に関連付けられたセッションのリスト。
     * 親エンティティが削除されると、関連付けられたセッションも削除されます（カスケード）。
     */
    @OneToMany(mappedBy = "bodyPart", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    /**
     * デフォルトコンストラクタ。
     */
    public BodyPart() {
    }

    /**
     * フィールドを指定してBodyPartオブジェクトを作成します。
     *
     * @param id トレーニング部位のID
     * @param name トレーニング部位の名前
     */
    public BodyPart(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * トレーニング部位のIDを取得します。
     *
     * @return トレーニング部位のID
     */
    public Long getId() {
        return id;
    }

    /**
     * トレーニング部位のIDを設定します。
     *
     * @param id トレーニング部位のID
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * トレーニング部位の名前を取得します。
     *
     * @return トレーニング部位の名前
     */
    public String getName() {
        return name;
    }

    /**
     * トレーニング部位の名前を設定します。
     *
     * @param name トレーニング部位の名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * このトレーニング部位に関連付けられたセッションのリストを取得します。
     *
     * @return セッションのリスト
     */
    public List<Session> getSessions() {
        return sessions;
    }

    /**
     * このトレーニング部位に関連付けられたセッションのリストを設定します。
     *
     * @param sessions セッションのリスト
     */
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}

