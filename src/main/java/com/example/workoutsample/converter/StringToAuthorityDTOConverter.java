package com.example.workoutsample.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.service.AuthorityService;

/**
 * String型のIDをAuthorityDTOオブジェクトに変換するコンバータークラスです。
 * Spring Frameworkの{@link Converter}を実装しています。
 */
@Component
public class StringToAuthorityDTOConverter implements Converter<String, AuthorityDTO> {

    private final AuthorityService authorityService;

    /**
     * AuthorityServiceを利用するためのコンストラクタ。
     *
     * @param authorityService AuthorityDTOを取得するためのサービス
     */
    public StringToAuthorityDTOConverter(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * String型のIDをAuthorityDTOオブジェクトに変換します。
     *
     * @param source String型のID（数値文字列）
     * @return 対応するAuthorityDTOオブジェクト
     * @throws NumberFormatException IDが数値に変換できない場合に発生
     */
    @Override
    public AuthorityDTO convert(@NonNull String source) {
        Long id = Long.valueOf(source);
        return authorityService.findAuthorityDTOById(id);
    }
}

