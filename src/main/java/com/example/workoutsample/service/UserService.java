package com.example.workoutsample.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.workoutsample.dto.UserDTO;
import com.example.workoutsample.mapper.UserMapper;
import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * ユーザーに関連するビジネスロジックを提供するサービスクラスです。
 * ユーザーの作成、更新、削除、検索などの操作をサポートします。
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private UserMapper userMapper;

    /**
     * すべてのユーザーを取得し、ID順にソートして返します。
     *
     * @return ユーザーのDTOリスト
     */
    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));
        return userMapper.toDTOList(users);
    }

    /**
     * 指定されたユーザー名に基づいてユーザーを検索します。
     *
     * @param username 検索対象のユーザー名
     * @return 検索結果のユーザーDTO
     */
    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.toDTO(user);
    }

    /**
     * 新しいユーザーを保存します。
     * 保存後、操作ログに記録します。
     *
     * @param userDTO 保存するユーザーのDTO
     * @param username 操作を行ったユーザーの名前
     * @return 保存されたユーザーのDTO
     */
    public UserDTO saveUser(UserDTO userDTO, String username) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        operationLogService.logOperation(username, "ユーザー(id=" + savedUser.getId() + ")を追加しました");
        return userMapper.toDTO(savedUser);
    }

    /**
     * 指定されたIDに基づいてユーザーを検索します。
     *
     * @param id 検索対象のユーザーID
     * @return 検索結果のユーザーDTO
     * @throws EntityNotFoundException 指定されたIDのユーザーが見つからない場合
     */
    public UserDTO findUserDTOById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    /**
     * 指定されたIDに基づいてエンティティ形式のユーザーを検索します。
     *
     * @param id 検索対象のユーザーID
     * @return 検索結果のユーザーエンティティ
     * @throws EntityNotFoundException 指定されたIDのユーザーが見つからない場合
     */
    public User findUserById(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    /**
     * ユーザー情報を更新します。
     * 更新後、操作ログに記録します。
     *
     * @param userDTO 更新するユーザーのDTO
     * @param username 操作を行ったユーザーの名前
     * @return 更新されたユーザーのDTO
     */
    public UserDTO updateUser(UserDTO userDTO, String username) {
        User user = userMapper.toEntity(userDTO);
        User updatedUser = userRepository.save(user);
        operationLogService.logOperation(username, "ユーザー(id=" + updatedUser.getId() + ")を編集しました");
        return userMapper.toDTO(updatedUser);
    }

    /**
     * 指定されたIDのユーザーを削除します（論理削除）。
     * 削除後、操作ログに記録します。
     *
     * @param id 削除するユーザーのID
     * @param username 操作を行ったユーザーの名前
     * @throws EntityNotFoundException 指定されたIDのユーザーが見つからない場合
     */
    public void deleteUserById(Long id, String username) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (user != null) {
            user.setDelete(true);
            userRepository.save(user);
            operationLogService.logOperation(username, "ユーザー(id=" + id + ")を削除しました");
        }
    }

    /**
     * ユーザー名または権限IDに基づいてユーザーを検索します。
     * ユーザー名および権限IDが両方指定されている場合、両方の条件を満たすユーザーを返します。
     *
     * @param username ユーザー名（部分一致検索に使用）
     * @param authorityId 権限ID
     * @return 検索条件に一致するユーザーのDTOリスト
     */
    public List<UserDTO> searchUsers(String username, Long authorityId) {
        List<User> users;
        if (username != null && !username.isEmpty() && authorityId != null) {
            users = userRepository.findByUsernameContainingAndAuthorities_Id(username, authorityId);
        } else if (username != null && !username.isEmpty()) {
            users = userRepository.findByUsernameContaining(username);
        } else if (authorityId != null) {
            users = userRepository.findByAuthorities_Id(authorityId);
        } else {
            users = userRepository.findAll();
        }

        users.sort(Comparator.comparing(User::getId));
        return userMapper.toDTOList(users);
    }
}
