package com.example.workoutsample.service;


import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import com.example.workoutsample.model.Exercise;
//import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogService operationLogService;

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));
        return users;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user, String username) {
        User savedUser = userRepository.save(user);
        operationLogService.logOperation(username, "ユーザー(id=" + user.getId() + ")を追加しました");
        return savedUser;
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User updateUser(User updateUser, String username) {
        operationLogService.logOperation(username, "ユーザー(id=" + updateUser.getId() + ")を編集しました");
        return userRepository.save(updateUser);
    }

    public void deleteUserById(Long id, String username) {
        User user = userRepository.findUserById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (user != null) {
            //bookにget
            user.setDelete(true);
            //isDleleteを変更
            userRepository.save(user);
            //saveで更新
            operationLogService.logOperation(username, "ユーザー(id=" + id+ ")を削除しました");
        }
    }

    // ユーザー名と権限でユーザーを検索するメソッド
    public List<User> searchUsers(String username, Long authorityId) {
        if (username != null && !username.isEmpty() && authorityId != null) {
            return userRepository.findByUsernameAndAuthorities_Id(username, authorityId);
        }
        // ユーザーネームのみが指定されている場合
        else if (username != null && !username.isEmpty()) {
            return userRepository.findByUsernameContaining(username);
        }
        // 権限のみが指定されている場合
        else if (authorityId != null) {
            return userRepository.findByAuthorities_Id(authorityId);
        }
        // 何も指定されていない場合
        else {
            return userRepository.findAll();
        }
    }

    /*public void deleteExerciseBySession(Session session) {
        List<Exercise> exercises = session.getExercises();
        for (Exercise exercise : exercises) {
            exercise.setDelete(true);
            exerciseRepository.save(exercise);
        }
    }

    public List<Exercise> findByName(String name) {
        return exerciseRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Exercise> getPastExercises(Exercise inputExercise) {
        List<Exercise> pastExercises = findByName(inputExercise.getName()).stream()
            .filter(exercise -> exercise.getSession().getDate().isBefore(inputExercise.getSession().getDate()))
            .collect(Collectors.toList());

        return pastExercises;
    }

    public boolean PRJudgmentOfReps(Exercise inputExercise) {
        boolean judgment = getPastExercises(inputExercise).stream()
            .filter(exerciseRepository.findByWeight(inputExercise.getWeight())::contains)
            .map(exercise -> exercise.getReps())
            .allMatch(reps -> reps < inputExercise.getReps());
        
        return judgment;
    }

    public boolean PRJudgmentOfWeight(Exercise inputExercise) {
        boolean judgment = getPastExercises(inputExercise).stream()
            .map(exercise -> exercise.getWeight())
            .allMatch(weight -> weight < inputExercise.getWeight());
        
        return judgment;
    }

    public void comparePRs(Exercise exercise) {
        if (PRJudgmentOfWeight(exercise)) {
            exercise.setWeightStatus("自己ベスト");
        }else if (PRJudgmentOfReps(exercise)) {
            exercise.setRepStatus("自己ベスト");
        }
    }*/
}
