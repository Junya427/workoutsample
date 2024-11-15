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

/**
 * ExerciseエンティティとExerciseDTOの間の変換を行うマッパークラスです。
 * DTOとエンティティ間のデータ変換ロジックを提供します。
 */
@Component
public class ExerciseMapper {

    private final UserService userService;

    /**
     * UserServiceを利用するためのコンストラクタ。
     *
     * @param userService ユーザー情報を取得するためのサービス
     */
    @Autowired
    public ExerciseMapper(UserService userService) {
        this.userService = userService;
    }

    /**
     * ExerciseエンティティをExerciseDTOに変換します。
     *
     * @param exercise Exerciseエンティティ
     * @return ExerciseDTOオブジェクト
     */
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
            exercise.getUser().getId(),
            exercise.isDeleted()
        );
    }

    /**
     * ExerciseエンティティのリストをExerciseDTOのリストに変換します。
     *
     * @param exercises Exerciseエンティティのリスト
     * @return ExerciseDTOのリスト
     */
    public List<ExerciseDTO> toDTOList(List<Exercise> exercises) {
        return exercises.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * ExerciseDTOをExerciseエンティティに変換します。
     *
     * @param exerciseDTO ExerciseDTOオブジェクト
     * @param session Exerciseが属するSessionエンティティ
     * @return Exerciseエンティティ
     */
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
            session,
            userService.findUserById(exerciseDTO.getUserId()),
            exerciseDTO.isDeleted()
        );
    }

    /**
     * ExerciseDTOのリストをExerciseエンティティのリストに変換します。
     *
     * @param exerciseDTOs ExerciseDTOのリスト
     * @param session Exerciseが属するSessionエンティティ
     * @return Exerciseエンティティのリスト
     */
    public List<Exercise> toEntityList(List<ExerciseDTO> exerciseDTOs, Session session) {
        if (exerciseDTOs == null) {
            return Collections.emptyList();
        }

        return exerciseDTOs.stream()
                .map(dto -> toEntity(dto, session))
                .collect(Collectors.toList());
    }
}
