package com.example.workoutsample.service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.mapper.ExerciseMapper;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.ExerciseRepository;

@Service
@Transactional
public class ExerciseService {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ExerciseMapper exerciseMapper;

    public List<Exercise> findAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();
        exercises.sort(Comparator.comparing(Exercise::getId));
        return exercises;
    }

    public List<Exercise> findByUser(User user) {
        List<Exercise> exercises = exerciseRepository.findByUser(user);
        exercises.sort(Comparator.comparing(Exercise::getId));
        return exercises;
    }

    public Exercise saveExercise(Exercise exercise, String username) {
        Exercise savedExercise = exerciseRepository.save(exercise);
        operationLogService.logOperation(username, "エクササイズ(id=" + exercise.getId() + ")を追加しました");
        return savedExercise;
    }

    public Exercise saveExercise(ExerciseDTO exerciseDTO, String username, Session session) {
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO, session);
        Exercise savedExercise = exerciseRepository.save(exercise);
        operationLogService.logOperation(username, "エクササイズ(id=" + exercise.getId() + ")を追加しました");
        return savedExercise;
    }

    public Optional<Exercise> findExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    public Exercise updateExercise(Exercise updateExercise, String username) {
        operationLogService.logOperation(username, "エクササイズ(id=" + updateExercise.getId() + ")を編集しました");
        return exerciseRepository.save(updateExercise);
    }

    public void deleteExerciseById(Long id, String username) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        if (optionalExercise.isPresent()) {
            //IDが見つかった場合
            Exercise exercise = optionalExercise.get();
            //bookにget
            exercise.setDelete(true);
            //isDleleteを変更
            exerciseRepository.save(exercise);
            //saveで更新
            operationLogService.logOperation(username, "エクササイズ(" + id + ")を削除しました");
        }
    }

    public void deleteExerciseBySession(Session session, String username) {
        List<Exercise> exercises = session.getExercises();
        for (Exercise exercise : exercises) {
            exercise.setDelete(true);
            exerciseRepository.save(exercise);
            operationLogService.logOperation(username, "エクササイズ(id= " + exercise.getId() + ")を削除しました");
        }
    }

    public List<Exercise> findByUserAndName(User loginUser, String name) {
        return exerciseRepository.findByUserAndNameContainingIgnoreCase(loginUser, name);
    }

    public List<Exercise> getPastExercises(User loginUser, ExerciseDTO inputExerciseDTO) {
        List<Exercise> pastExercises = findByUserAndName(loginUser, inputExerciseDTO.getName());

        return pastExercises;
    }

    public boolean PRJudgmentOfReps(User loginUser, ExerciseDTO inputExerciseDTO) {
        return getPastExercises(loginUser, inputExerciseDTO).stream()
            .filter(exercise -> exerciseRepository.findByWeight(inputExerciseDTO.getWeight()).contains(exercise))
            .map(Exercise::getReps)
            .allMatch(reps -> reps < inputExerciseDTO.getReps());
    }
    

    public boolean PRJudgmentOfWeight(User loginUser, ExerciseDTO inputExerciseDTO) {
        return getPastExercises(loginUser, inputExerciseDTO).stream()
            .map(Exercise::getWeight)
            .allMatch(weight -> weight < inputExerciseDTO.getWeight());
    }
    

    public void comparePRs(User loginUser, ExerciseDTO exerciseDTO) {
        if (PRJudgmentOfWeight(loginUser, exerciseDTO)) {
            exerciseDTO.setWeightStatus("自己ベスト");
        }else if (PRJudgmentOfReps(loginUser, exerciseDTO)) {
            exerciseDTO.setRepStatus("自己ベスト");
        }
    }
}
