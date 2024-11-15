package com.example.workoutsample.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.dto.ExerciseDTO;
import com.example.workoutsample.dto.SessionDTO;
import com.example.workoutsample.mapper.ExerciseMapper;
import com.example.workoutsample.mapper.SessionMapper;
import com.example.workoutsample.model.BodyPart;
import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.BodyPartRepository;
import com.example.workoutsample.repository.SessionRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * セッションに関連するビジネスロジックを提供するサービスクラスです。
 * セッションの作成、更新、削除、検索などの操作をサポートします。
 */
@Service
@Transactional
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private BodyPartRepository bodyPartRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private ExerciseMapper exerciseMapper;

    /**
     * すべてのセッションを取得し、ID順にソートして返します。
     *
     * @return セッションのDTOリスト
     */
    public List<SessionDTO> findAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        sessions.sort(Comparator.comparing(Session::getId));
        return sessionMapper.toDTOList(sessions);
    }

    /**
     * 指定されたユーザーに関連付けられたすべてのセッションを取得します。
     * ID順にソートして返します。
     *
     * @param user セッションを検索する対象のユーザー
     * @return 指定されたユーザーに関連付けられたセッションのDTOリスト
     */
    public List<SessionDTO> findByUser(User user) {
        List<Session> sessions = sessionRepository.findByUser(user);
        sessions.sort(Comparator.comparing(Session::getId));
        return sessionMapper.toDTOList(sessions);
    }

    /**
     * 新しいセッションを保存します。
     * 保存後、操作ログに記録します。
     *
     * @param sessionDTO 保存するセッションのDTO
     * @param username 操作を行ったユーザーの名前
     * @return 保存されたセッションのエンティティ
     */
    public Session saveSession(SessionDTO sessionDTO, String username) {
        Session session = sessionMapper.toEntity(sessionDTO);
        Session savedSession = sessionRepository.save(session);
        operationLogService.logOperation(username, "セッション(id=" + session.getId() + ")を追加しました");
        return savedSession;
    }

    /**
     * 指定されたIDのセッションを検索し、DTO形式で返します。
     *
     * @param id 検索するセッションのID
     * @return 検索結果のセッションDTO（存在しない場合は空のOptional）
     */
    public Optional<SessionDTO> findSessionDTOById(Long id) {
        return sessionRepository.findById(id).map(sessionMapper::toDTO);
    }

    /**
     * 指定されたセッションIDに関連付けられたエクササイズを取得し、
     * ID順にソートしてDTO形式で返します。
     *
     * @param sessionId セッションID
     * @return エクササイズのDTOリスト
     */
    public List<ExerciseDTO> getSortedExercisesDTOBySessionId(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + sessionId));

        return session.getExercises().stream()
            .sorted(Comparator.comparing(Exercise::getId))
            .map(exerciseMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * セッションを更新します。
     * 更新後、操作ログに記録します。
     *
     * @param updateSessionDTO 更新するセッションのDTO
     * @param username 操作を行ったユーザーの名前
     * @return 更新されたセッションのDTO
     */
    public SessionDTO updateSession(SessionDTO updateSessionDTO, String username) {
        Session session = sessionMapper.toEntity(updateSessionDTO);
        Session savedSession = sessionRepository.save(session);
        operationLogService.logOperation(username, "セッション(id=" + savedSession.getId() + ")を編集しました");
        return sessionMapper.toDTO(savedSession);
    }

    /**
     * 指定されたIDのセッションを削除します（論理削除）。
     * 削除後、操作ログに記録します。
     *
     * @param id 削除するセッションのID
     * @param username 操作を行ったユーザーの名前
     */
    public void deleteSessionById(Long id, String username) {
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isPresent()) {
            //IDが見つかった場合
            Session session = optionalSession.get();
            //bookにget
            session.setDelete(true);
            //isDleleteを変更
            sessionRepository.save(session);
            //saveで更新
            operationLogService.logOperation(username, "セッション(id=" + id + ")を削除しました");
        }
    }

    /**
     * 指定されたユーザーと日付に基づいてセッションを検索します。
     *
     * @param loginUser 検索する対象のユーザー
     * @param date 検索する日付
     * @return 条件に一致するセッションのDTOリスト
     */
    public List<SessionDTO> findByUserAndDate(User loginUser, LocalDate date) {
        return sessionRepository.findByUserAndDate(loginUser, date).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * 指定されたユーザーとボディパートに基づいてセッションを検索します。
     *
     * @param loginUser 検索する対象のユーザー
     * @param bodyPartId ボディパートのID
     * @return 条件に一致するセッションのDTOリスト
     */
    public List<SessionDTO> findByUserAndBodyPart(User loginUser, Long bodyPartId) {
        BodyPart bodyPart = bodyPartRepository.findById(bodyPartId)
            .orElseThrow();
        return sessionRepository.findByUserAndBodyPart(loginUser, bodyPart).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * 指定されたユーザー、日付、およびボディパートに基づいてセッションを検索します。
     *
     * @param loginUser 検索する対象のユーザー
     * @param date 検索する日付
     * @param bodyPartId ボディパートのID
     * @return 条件に一致するセッションのDTOリスト
     */
    public List<SessionDTO> findByUserAndDateAndBodyPart(User loginUser, LocalDate date, Long bodyPartId) {
        BodyPart bodyPart = bodyPartRepository.findById(bodyPartId)
            .orElseThrow();
        return sessionRepository.findByUserAndDateAndBodyPart(loginUser, date, bodyPart).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * 指定されたセッションIDに関連付けられたすべてのエクササイズの
     * トータルボリュームを計算して返します。
     *
     * @param sessionId セッションID
     * @return トータルボリューム
     */
    public int calculateTotalVolume(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        return Optional.ofNullable(session.getExercises())
            .orElse(Collections.emptyList())
            .stream()
            .mapToInt(exercise -> exercise.getVolume().intValue())
            .sum();
    }

    /**
     * 現在のトレーニング量と過去のトレーニング量を比較し、結果を文字列で返します。
     *
     * @param loginUser 検索する対象のユーザー
     * @param inputSessionDTO 現在のセッションのDTO
     * @param inputTrainingVolume 現在のトレーニング量
     * @return 比較結果のメッセージ
     */
    public String comparisonTrainingVolume(User loginUser, SessionDTO inputSessionDTO, int inputTrainingVolume) {
        List<SessionDTO> sessionDTOs = findByUserAndBodyPart(loginUser, inputSessionDTO.getBodyPart().getId());
    
        Optional<SessionDTO> closestPastSessionOpt = getClosestPastSession(sessionDTOs, inputSessionDTO.getDate());
    
        if (!closestPastSessionOpt.isPresent()) {
            return "初回の" + inputSessionDTO.getBodyPart().getName() + "トレーニングです。お疲れ様でした！";
        }
    
        SessionDTO closestPastSession = closestPastSessionOpt.get();
    
        int comparisonTrainingVolume = inputTrainingVolume - calculateTotalVolume(closestPastSession.getId());
        String differenceTrainingVolume;
        String str = "前回の" + inputSessionDTO.getBodyPart().getName() + "トレ";
    
        if (comparisonTrainingVolume > 0) {
            differenceTrainingVolume = str + "より" + comparisonTrainingVolume + "多いです";
        } else if (comparisonTrainingVolume < 0) {
            differenceTrainingVolume = str + "より" + Math.abs(comparisonTrainingVolume) + "少ないです";
        } else {
            differenceTrainingVolume = str + "と同じです";
        }
    
        return differenceTrainingVolume;
    }

    /**
     * 指定された日付より過去のセッションの中から、最も近いセッションを取得します。
     *
     * @param sessionDTOs 検索対象のセッションDTOリスト
     * @param referenceDate 参照日付
     * @return 最も近いセッションDTO（存在しない場合は空のOptional）
     */
    public Optional<SessionDTO> getClosestPastSession(List<SessionDTO> sessionDTOs, LocalDate referenceDate) {
        return sessionDTOs.stream()
            .filter(session -> session.getDate().isBefore(referenceDate))
            .max(Comparator.comparing(SessionDTO::getDate));
    }

    /**
     * 指定されたセッションが新規作成されたものであるかを判定します。
     *
     * @param session 判定対象のセッション
     * @return 新規作成の場合はtrue、それ以外はfalse
     */
    public boolean isNewSession(Session session) {
        return session.getId() == null;
    }
}
