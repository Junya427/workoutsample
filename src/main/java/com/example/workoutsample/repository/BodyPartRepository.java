package com.example.workoutsample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.workoutsample.model.BodyPart;

@Repository
public interface BodyPartRepository extends JpaRepository<BodyPart, Long> {

}
