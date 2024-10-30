package com.example.workoutsample.mapper;

import com.example.workoutsample.dto.BodyPartDTO;
import com.example.workoutsample.model.BodyPart;

public class BodyPartMapper {

    public static BodyPartDTO toDTO(BodyPart bodyPart) {
        if (bodyPart == null) {
            return null;
        }

        return new BodyPartDTO(
            bodyPart.getId(),
            bodyPart.getName()
        );
    }
    
    public static BodyPart toEntity(BodyPartDTO bodyPartDTO) {
        if (bodyPartDTO == null) {
            return null;
        }

        BodyPart bodyPart = new BodyPart();
        bodyPart.setId(bodyPartDTO.getId());
        bodyPart.setName(bodyPartDTO.getName());

        return bodyPart;
    }
}

