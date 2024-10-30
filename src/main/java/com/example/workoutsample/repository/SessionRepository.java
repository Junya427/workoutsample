package com.example.workoutsample.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.workoutsample.model.BodyPart;
import com.example.workoutsample.model.Session;
import com.example.workoutsample.model.User;

public interface SessionRepository extends JpaRepository<Session, Long>{
    List<Session> findByUserAndDate(User loginUser, LocalDate date);

    List<Session> findByUserAndBodyPart(User loginUser, BodyPart bodyPart);

    List<Session> findByUserAndDateAndBodyPart(User loginUser, LocalDate date, BodyPart bodyPart);

    List<Session> findByUser(User user);
}
