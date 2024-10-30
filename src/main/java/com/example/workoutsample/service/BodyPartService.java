package com.example.workoutsample.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.workoutsample.dto.BodyPartDTO;
import com.example.workoutsample.mapper.BodyPartMapper;
import com.example.workoutsample.model.BodyPart;
import com.example.workoutsample.repository.BodyPartRepository;

@Service
public class BodyPartService {
    @Autowired
    private BodyPartRepository bodyPartRepository;

    public List<BodyPart> findAllBodyParts() {
        List<BodyPart> bodyParts = bodyPartRepository.findAll();
        bodyParts.sort(Comparator.comparing(BodyPart::getId));
        return bodyParts;
    }

    public List<BodyPartDTO> findAllBodyPartDTOs() {
        return bodyPartRepository.findAll().stream()
            .map(BodyPartMapper::toDTO)
            .sorted(Comparator.comparing(BodyPartDTO::getId))
            .collect(Collectors.toList());
    }
}