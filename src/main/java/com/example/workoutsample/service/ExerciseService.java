package com.example.workoutsample.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.dto.SessionDTO;
import com.example.workoutsample.mapper.ExerciseMapper;
import com.example.workoutsample.mapper.SessionMapper;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.ExerciseRepository;

/**
 * エクササイズに関するビジネスロジックを提供するサービスクラスです。
 * エクササイズの作成、更新、削除、検索などの操作をサポートします。
 */
@Service
@Transactional
public class ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private SessionMapper sessionMapper;

    /**
     * すべてのエクササイズを取得し、ID順にソートして返します。
     *
     * @return {@link Exercise}のリスト
     */
    public List<Exercise> findAllExercises() {
        List<Exercise> exercises = exerciseRepository.findAll();
        exercises.sort(Comparator.comparing(Exercise::getId));
        return exercises;
    }

    /**
     * 指定されたユーザーに関連するすべてのエクササイズを取得し、ID順にソートして返します。
     *
     * @param user ユーザー
     * @return {@link Exercise}のリスト
     */
    public List<Exercise> findByUser(User user) {
        List<Exercise> exercises = exerciseRepository.findByUser(user);
        exercises.sort(Comparator.comparing(Exercise::getId));
        return exercises;
    }

    /**
     * 新しいエクササイズを保存します。
     * 保存後、操作ログに記録します。
     *
     * @param exercise 保存するエクササイズ
     * @param username 操作を行ったユーザーの名前
     * @return 保存された{@link Exercise}
     */
    public Exercise saveExercise(Exercise exercise, String username) {
        Exercise savedExercise = exerciseRepository.save(exercise);
        operationLogService.logOperation(username, "エクササイズ(id=" + exercise.getId() + ")を追加しました");
        return savedExercise;
    }

    /**
     * DTO形式のエクササイズを保存します。
     * セッション情報も関連付けられます。
     * 保存後、操作ログに記録します。
     *
     * @param exerciseDTO 保存するエクササイズDTO
     * @param username 操作を行ったユーザーの名前
     * @param sessionDTO 関連付けられるセッションDTO
     * @return 保存された{@link Exercise}
     */
    public Exercise saveExercise(ExerciseDTO exerciseDTO, String username, SessionDTO sessionDTO) {
        Session session = sessionMapper.toEntity(sessionDTO);
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO, session);
        Exercise savedExercise = exerciseRepository.save(exercise);
        operationLogService.logOperation(username, "エクササイズ(id=" + exercise.getId() + ")を追加しました");
        return savedExercise;
    }

    /**
     * 指定されたIDのエクササイズを検索します。
     *
     * @param id 検索対象のエクササイズID
     * @return 検索結果を含む{@link Optional}
     */
    public Optional<Exercise> findExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    /**
     * エクササイズを更新します。
     * 更新後、操作ログに記録します。
     *
     * @param updateExercise 更新するエクササイズ
     * @param username 操作を行ったユーザーの名前
     * @return 更新された{@link Exercise}
     */
    public Exercise updateExercise(Exercise updateExercise, String username) {
        operationLogService.logOperation(username, "エクササイズ(id=" + updateExercise.getId() + ")を編集しました");
        return exerciseRepository.save(updateExercise);
    }

    /**
     * 指定されたIDのエクササイズを削除します（論理削除）。
     * 削除後、操作ログに記録します。
     *
     * @param id 削除するエクササイズのID
     * @param username 操作を行ったユーザーの名前
     */
    public void deleteExerciseById(Long id, String username) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        if (optionalExercise.isPresent()) {
            Exercise exercise = optionalExercise.get();
            exercise.setDelete(true); // 論理削除
            exerciseRepository.save(exercise);
            operationLogService.logOperation(username, "エクササイズ(" + id + ")を削除しました");
        }
    }

    /**
     * 指定されたセッションに関連付けられたすべてのエクササイズを削除します（論理削除）。
     * 削除後、操作ログに記録します。
     *
     * @param sessionDTO 削除対象のセッションDTO
     * @param username 操作を行ったユーザーの名前
     */
    public void deleteExerciseBySession(SessionDTO sessionDTO, String username) {
        List<ExerciseDTO> exerciseDTOs = sessionDTO.getExercises();

        if (exerciseDTOs != null) {
            for (ExerciseDTO exerciseDTO : exerciseDTOs) {
                exerciseDTO.setDeleted(true);
                Session session = sessionMapper.toEntity(sessionDTO);
                Exercise exercise = exerciseMapper.toEntity(exerciseDTO, session);
                exerciseRepository.save(exercise);
                operationLogService.logOperation(username, "エクササイズ(id= " + exercise.getId() + ")を削除しました");
            }
        }
    }

    /**
     * ユーザーとエクササイズ名に基づいてエクササイズを検索します。
     * 大文字小文字を区別しません。
     *
     * @param loginUser ユーザー
     * @param name エクササイズ名
     * @return 条件に一致する{@link Exercise}のリスト
     */
    public List<Exercise> findByUserAndName(User loginUser, String name) {
        return exerciseRepository.findByUserAndNameContainingIgnoreCase(loginUser, name);
    }

    /**
     * 過去に記録されたエクササイズを取得します。
     *
     * @param loginUser ユーザー
     * @param inputExerciseDTO 検索条件となるエクササイズDTO
     * @return 過去の{@link Exercise}のリスト
     */
    public List<Exercise> getPastExercises(User loginUser, ExerciseDTO inputExerciseDTO) {
        return findByUserAndName(loginUser, inputExerciseDTO.getName());
    }

    /**
     * 挙上回数が自己ベストであるかを判定します。
     *
     * @param loginUser ユーザー
     * @param inputExerciseDTO 判定対象のエクササイズDTO
     * @return trueの場合、自己ベスト
     */
    public boolean PRJudgmentOfReps(User loginUser, ExerciseDTO inputExerciseDTO) {
        return getPastExercises(loginUser, inputExerciseDTO).stream()
            .filter(exercise -> exerciseRepository.findByWeight(inputExerciseDTO.getWeight()).contains(exercise))
            .map(Exercise::getReps)
            .allMatch(reps -> reps < inputExerciseDTO.getReps());
    }

    /**
     * 重量が自己ベストであるかを判定します。
     *
     * @param loginUser ユーザー
     * @param inputExerciseDTO 判定対象のエクササイズDTO
     * @return trueの場合、自己ベスト
     */
    public boolean PRJudgmentOfWeight(User loginUser, ExerciseDTO inputExerciseDTO) {
        return getPastExercises(loginUser, inputExerciseDTO).stream()
            .map(Exercise::getWeight)
            .allMatch(weight -> weight < inputExerciseDTO.getWeight());
    }

    /**
     * 自己ベストの比較を行い、結果をDTOに設定します。
     *
     * @param loginUser ユーザー
     * @param exerciseDTO 判定対象のエクササイズDTO
     */
    public void comparePRs(User loginUser, ExerciseDTO exerciseDTO) {
        if (PRJudgmentOfWeight(loginUser, exerciseDTO)) {
            exerciseDTO.setWeightStatus("自己ベスト");
        } else if (PRJudgmentOfReps(loginUser, exerciseDTO)) {
            exerciseDTO.setRepStatus("自己ベスト");
        }
    }
}
