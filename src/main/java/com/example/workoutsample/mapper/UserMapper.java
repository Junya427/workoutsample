package com.example.workoutsample.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.workoutsample.dto.AuthorityDTO;
import com.example.workoutsample.dto.UserDTO;
import com.example.workoutsample.model.Authority;
import com.example.workoutsample.model.User;

/**
 * UserエンティティとUserDTOの間の変換を行うマッパークラスです。
 * 権限情報を含む複雑なデータ構造をDTOとエンティティ間で変換します。
 */
@Component
public class UserMapper {

    @Autowired
    private AuthorityMapper authorityMapper;

    /**
     * UserエンティティをUserDTOに変換します。
     *
     * @param user Userエンティティ
     * @return UserDTOオブジェクト
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        Set<AuthorityDTO> authorities = user.getAuthorities().stream()
                .map(authorityMapper::toDTO)
                .collect(Collectors.toSet());
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                authorities
        );
    }

    /**
     * UserエンティティのリストをUserDTOのリストに変換します。
     *
     * @param users Userエンティティのリスト
     * @return UserDTOのリスト
     */
    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * UserDTOをUserエンティティに変換します。
     *
     * @param userDTO UserDTOオブジェクト
     * @return Userエンティティ
     */
    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEnabled(userDTO.isEnabled());
        
        // AuthorityDTOをAuthorityエンティティに変換してセット
        Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityMapper::toEntity)
                .collect(Collectors.toSet());
        user.setAuthorities(authorities);
        return user;
    }
}
