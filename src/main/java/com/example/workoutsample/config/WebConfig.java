package com.example.workoutsample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.workoutsample.converter.StringToAuthorityDTOConverter;

/**
 * Webアプリケーション全体の設定を行うクラスです。
 * Spring MVCのカスタム設定やコンバーターの登録などを行います。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final StringToAuthorityDTOConverter stringToAuthorityDTOConverter;

    /**
     * コンストラクタ。依存性注入により、カスタムコンバーターを設定します。
     *
     * @param stringToAuthorityDTOConverter {@link StringToAuthorityDTOConverter} インスタンス
     */
    public WebConfig(StringToAuthorityDTOConverter stringToAuthorityDTOConverter) {
        this.stringToAuthorityDTOConverter = stringToAuthorityDTOConverter;
    }

    /**
     * カスタムフォーマッターやコンバーターを登録します。
     *
     * @param registry フォーマッターやコンバーターを登録するための {@link FormatterRegistry}
     */
    @Override
    public void addFormatters(@NonNull FormatterRegistry registry) {
        registry.addConverter(stringToAuthorityDTOConverter);
    }
}

