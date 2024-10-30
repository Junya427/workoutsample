package com.example.workoutsample.service;


import java.util.Comparator;
import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.model.Authority;
//import com.example.workoutsample.model.Session;
import com.example.workoutsample.repository.AuthorityRepository;

@Service
@Transactional
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Authority> findAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        authorities.sort(Comparator.comparing(Authority::getId));
        return authorities;
    }


    /*public Exercise saveExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    public Optional<Exercise> findExerciseById(Long id) {
        return exerciseRepository.findById(id);
    }

    public Exercise updateExercise(Exercise updateExercise) {
        return exerciseRepository.save(updateExercise);
    }

    public void deleteExerciseById(Long id) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);
        if (optionalExercise.isPresent()) {
            //IDが見つかった場合
            Exercise exercise = optionalExercise.get();
            //bookにget
            exercise.setDelete(true);
            //isDleleteを変更
            exerciseRepository.save(exercise);
            //saveで更新
        }
    }

    public void deleteExerciseBySession(Session session) {
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

