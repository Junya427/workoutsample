package com.example.workoutsample.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.mapper.AuthorityMapper;
import com.example.workoutsample.model.Authority;
import com.example.workoutsample.repository.AuthorityRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityMapper authorityMapper;

    public List<AuthorityDTO> findAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        authorities.sort(Comparator.comparing(Authority::getId));
        return authorityMapper.toDTOList(authorities);
    }

    public AuthorityDTO findAuthorityDTOById(Long id) {
        Authority authority = authorityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Authority not found with id: " + id));
        return authorityMapper.toDTO(authority);
    }
}


