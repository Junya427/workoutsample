package com.example.workoutsample.dto;

import jakarta.validation.constraints.NotNull;

public class BodyPartDTO {

    @NotNull(message = "部位を選択してください")
    private Long id;
    private String name;

    public BodyPartDTO() {
    }
    public BodyPartDTO(Long id, String name) {
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
    
}
