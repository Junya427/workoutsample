package com.example.workoutsample.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.model.Authority;

@Component
public class AuthorityMapper {

    public AuthorityDTO toDTO(Authority authority) {
        if (authority == null) {
            return null;
        }
        return new AuthorityDTO(authority.getId(), authority.getAuthority());
    }

    public List<AuthorityDTO> toDTOList(List<Authority> authorities) {
        return authorities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Authority toEntity(AuthorityDTO authorityDTO) {
        if (authorityDTO == null) {
            return null;
        }
        Authority authority = new Authority();
        authority.setId(authorityDTO.getId());
        authority.setAuthority(authorityDTO.getAuthority());
        return authority;
    }
}

