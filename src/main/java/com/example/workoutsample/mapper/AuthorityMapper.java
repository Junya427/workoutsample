package com.example.workoutsample.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.model.Authority;

/**
 * AuthorityエンティティとAuthorityDTOの間の変換を行うマッパークラスです。
 * DTOとエンティティ間のデータ変換ロジックを提供します。
 */
@Component
public class AuthorityMapper {

    /**
     * AuthorityエンティティをAuthorityDTOに変換します。
     *
     * @param authority Authorityエンティティ
     * @return AuthorityDTOオブジェクト
     */
    public AuthorityDTO toDTO(Authority authority) {
        if (authority == null) {
            return null;
        }
        return new AuthorityDTO(authority.getId(), authority.getAuthority());
    }

    /**
     * AuthorityエンティティのリストをAuthorityDTOのリストに変換します。
     *
     * @param authorities Authorityエンティティのリスト
     * @return AuthorityDTOのリスト
     */
    public List<AuthorityDTO> toDTOList(List<Authority> authorities) {
        return authorities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * AuthorityDTOをAuthorityエンティティに変換します。
     *
     * @param authorityDTO AuthorityDTOオブジェクト
     * @return Authorityエンティティ
     */
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

