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

@Entity
@Table(name = "bodyParts")
public class BodyPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "bodyPart", cascade = CascadeType.ALL)//BookクラスのManyToOneで設定した変数genreをmappedByする。
    //cascadeは連なっているという意味。親が削除されたら紐づくこも削除される。
    private List<Session> sessions = new ArrayList<>();

    public BodyPart(){
    }

    public BodyPart(Long id, String name) {
        this.id = id;
        this.name = name;
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

    public List<Session> getSessions() {
        return sessions;
    }
    
    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }
}
