package com.example.workoutsample.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.service.UserService;

@Component
public class ExerciseMapper {

    private final UserService userService;

    @Autowired
    public ExerciseMapper(UserService userService) {
        this.userService = userService;
    }

    public ExerciseDTO toDTO(Exercise exercise) {
        if (exercise == null) {
            return null;
        }
    
        return new ExerciseDTO(
            exercise.getId(),
            exercise.getName(),
            exercise.getWeight(),
            exercise.getReps(),
            exercise.getSets(),
            exercise.getVolume(),
            exercise.getWeightStatus(),
            exercise.getRepStatus(),
            exercise.getSession().getId(),
            exercise.getUser().getId()
        );
    }

    public List<ExerciseDTO> toDTOList(List<Exercise> exercises) {
        return exercises.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Exercise toEntity(ExerciseDTO exerciseDTO, Session session) {
        if (exerciseDTO == null) {
            return null;
        }
        return new Exercise(
            exerciseDTO.getId(),
            exerciseDTO.getName(),
            exerciseDTO.getWeight(),
            exerciseDTO.getReps(),
            exerciseDTO.getSets(),
            exerciseDTO.getWeightStatus(),
            exerciseDTO.getRepStatus(),
            session, // セッションを引数として渡す
            userService.findUserById(exerciseDTO.getUserId())
        );
    }
    

    public List<Exercise> toEntityList(List<ExerciseDTO> exerciseDTOs, Session session) {
        if (exerciseDTOs == null) {
            return Collections.emptyList(); // nullの場合は空のリストを返す
        }
    
        return exerciseDTOs.stream()
                .map(dto -> toEntity(dto, session)) // セッションを渡してエクササイズを変換
                .collect(Collectors.toList());
    }
    
}

