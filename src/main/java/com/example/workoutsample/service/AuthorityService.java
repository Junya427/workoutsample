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

/**
 * 権限に関するビジネスロジックを提供するサービスクラスです。
 * 権限エンティティの検索およびDTOへの変換をサポートします。
 */
@Service
@Transactional
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * データベース内のすべての権限を取得し、IDでソートされたDTOリストを返します。
     *
     * @return {@link AuthorityDTO}のリスト
     */
    public List<AuthorityDTO> findAllAuthorities() {
        List<Authority> authorities = authorityRepository.findAll();
        authorities.sort(Comparator.comparing(Authority::getId));
        return authorityMapper.toDTOList(authorities);
    }

    /**
     * 指定されたIDに一致する権限を検索し、DTOとして返します。
     * 権限が見つからない場合、{@link EntityNotFoundException}をスローします。
     *
     * @param id 検索対象の権限ID
     * @return IDに一致する{@link AuthorityDTO}
     * @throws EntityNotFoundException 権限が見つからない場合
     */
    public AuthorityDTO findAuthorityDTOById(Long id) {
        Authority authority = authorityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Authority not found with id: " + id));
        return authorityMapper.toDTO(authority);
    }
}

