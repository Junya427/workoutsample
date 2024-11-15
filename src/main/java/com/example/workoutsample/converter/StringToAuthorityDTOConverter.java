package com.example.workoutsample.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.service.AuthorityService;

@Component
public class StringToAuthorityDTOConverter implements Converter<String, AuthorityDTO> {

    private final AuthorityService authorityService;

    public StringToAuthorityDTOConverter(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public AuthorityDTO convert(@NonNull String source) {
        Long id = Long.valueOf(source);
        return authorityService.findAuthorityDTOById(id);
    }
}

