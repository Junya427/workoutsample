package com.example.workoutsample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.workoutsample.converter.StringToAuthorityDTOConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToAuthorityDTOConverter stringToAuthorityDTOConverter;

    public WebConfig(StringToAuthorityDTOConverter stringToAuthorityDTOConverter) {
        this.stringToAuthorityDTOConverter = stringToAuthorityDTOConverter;
    }

    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(stringToAuthorityDTOConverter);
    }
}

