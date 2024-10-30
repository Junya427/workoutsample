package com.example.workoutsample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.Exercise;
import com.example.workoutsample.model.User;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
    List<Exercise> findByUserAndName(User loginUser, String name);

    //@Query("SELECT e FROM Exercise e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Exercise> findByUserAndNameContainingIgnoreCase(User loginUser, String name);

    List<Exercise> findByReps(Long reps);

    List<Exercise> findByWeight(Long weight);

    List<Exercise> findByUser(User user);
}
