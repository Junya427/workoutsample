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

    public List<SessionDTO> findAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        sessions.sort(Comparator.comparing(Session::getId));
        return sessionMapper.toDTOList(sessions);
    }

    public List<SessionDTO> findByUser(User user) {
        List<Session> sessions = sessionRepository.findByUser(user);
        sessions.sort(Comparator.comparing(Session::getId));
        return sessionMapper.toDTOList(sessions);
    }

    public Session saveSession(SessionDTO sessionDTO, String username) {
        Session session = sessionMapper.toEntity(sessionDTO);
        Session savedSession = sessionRepository.save(session);
        operationLogService.logOperation(username, "セッション(id=" + session.getId() + ")を追加しました");
        return savedSession;
    }

    public Optional<SessionDTO> findSessionDTOById(Long id) {
        return sessionRepository.findById(id).map(sessionMapper::toDTO);
    }

    public List<ExerciseDTO> getSortedExercisesDTOBySessionId(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
            .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + sessionId));

        return session.getExercises().stream()
            .sorted(Comparator.comparing(Exercise::getId))
            .map(exerciseMapper::toDTO)
            .collect(Collectors.toList());
    }

    public SessionDTO updateSession(SessionDTO updateSessionDTO, String username) {
        Session session = sessionMapper.toEntity(updateSessionDTO);
        Session savedSession = sessionRepository.save(session);
        operationLogService.logOperation(username, "セッション(id=" + savedSession.getId() + ")を編集しました");
        return sessionMapper.toDTO(savedSession);
    }

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

    /*public List<SessionDTO> searchSessions(User user, LocalDate date, Long bodyPartId) {
        System.out.println("--------");
        System.out.println("ユーザー名" + user.getUsername());
        System.out.println("日時" + date);
        System.out.println("部位" + bodyPartId);
        System.out.println("--------");
        return sessionRepository.findAll().stream()
            .filter(session -> user == null || session.getUser().equals(user))
            .filter(session -> date == null || session.getDate().equals(date))
            .filter(session -> bodyPartId == null || session.getBodyPart().getId().equals(bodyPartId))
            .map(sessionMapper::toDTO)
            .toList();
    }*/
    

    public List<SessionDTO> findByUserAndDate(User loginUser, LocalDate date) {
        return sessionRepository.findByUserAndDate(loginUser, date).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<SessionDTO> findByUserAndBodyPart(User loginUser, Long bodyPartId) {
        BodyPart bodyPart = bodyPartRepository.findById(bodyPartId)
            .orElseThrow();
        return sessionRepository.findByUserAndBodyPart(loginUser, bodyPart).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<SessionDTO> findByUserAndDateAndBodyPart(User loginUser, LocalDate date, Long bodyPartId) {
        BodyPart bodyPart = bodyPartRepository.findById(bodyPartId)
            .orElseThrow();
        return sessionRepository.findByUserAndDateAndBodyPart(loginUser, date, bodyPart).stream()
            .map(sessionMapper::toDTO)
            .collect(Collectors.toList());//ここやった
    }

    public int calculateTotalVolume(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow();
        return Optional.ofNullable(session.getExercises())
            .orElse(Collections.emptyList())
            .stream()
            .mapToInt(exercise -> exercise.getVolume().intValue())
            .sum();
    }

    public String comparisonTrainingVolume(User loginUser, SessionDTO inputSessionDTO, int inputTrainingVolume) {
        // 指定されたボディパートに対応する過去のセッションを取得
        List<SessionDTO> sessionDTOs = findByUserAndBodyPart(loginUser, inputSessionDTO.getBodyPart().getId());
    
        // 過去のセッションの中から最も近いセッションを取得
        Optional<SessionDTO> closestPastSessionOpt = getClosestPastSession(sessionDTOs, inputSessionDTO.getDate());
    
        // 過去のセッションが存在しない場合、初回のトレーニングとしてメッセージを返す
        if (!closestPastSessionOpt.isPresent()) {
            return "初回の" + inputSessionDTO.getBodyPart().getName() + "トレーニングです。お疲れ様でした！";
        }
    
        // 過去のセッションが存在する場合
        SessionDTO closestPastSession = closestPastSessionOpt.get();
    
        // 前回のトレーニング量との差を計算
        int comparisonTrainingVolume = inputTrainingVolume - calculateTotalVolume(closestPastSession.getId());
        String differenceTrainingVolume;
        String str = "前回の" + inputSessionDTO.getBodyPart().getName() + "トレ";
    
        // トレーニング量の差に基づくメッセージを作成
        if (comparisonTrainingVolume > 0) {
            differenceTrainingVolume = str + "より" + comparisonTrainingVolume + "多いです";
        } else if (comparisonTrainingVolume < 0) {
            differenceTrainingVolume = str + "より" + Math.abs(comparisonTrainingVolume) + "少ないです";
        } else {
            differenceTrainingVolume = str + "と同じです";
        }
    
        return differenceTrainingVolume;
    }

    public Optional<SessionDTO> getClosestPastSession(List<SessionDTO> sessionDTOs, LocalDate referenceDate) {
        return sessionDTOs.stream()
            .filter(session -> session.getDate().isBefore(referenceDate))
            .max(Comparator.comparing(SessionDTO::getDate));
    }

    public boolean isNewSession(Session session) {
        return session.getId() == null; // IDがnullの場合は新規作成
    }
}