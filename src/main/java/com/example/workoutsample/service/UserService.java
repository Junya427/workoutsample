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

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private UserMapper userMapper;

    public List<UserDTO> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));
        return userMapper.toDTOList(users);
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return userMapper.toDTO(user);
    }

    public UserDTO saveUser(UserDTO userDTO, String username) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        operationLogService.logOperation(username, "ユーザー(id=" + savedUser.getId() + ")を追加しました");
        return userMapper.toDTO(savedUser);
    }

    public UserDTO findUserDTOById(Long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public UserDTO updateUser(UserDTO userDTO, String username) {
        User user = userMapper.toEntity(userDTO);
        User updatedUser = userRepository.save(user);
        operationLogService.logOperation(username, "ユーザー(id=" + updatedUser.getId() + ")を編集しました");
        return userMapper.toDTO(updatedUser);
    }

    public void deleteUserById(Long id, String username) {
        User user = userRepository.findUserById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        if (user != null) {
            user.setDelete(true);
            userRepository.save(user);
            operationLogService.logOperation(username, "ユーザー(id=" + id + ")を削除しました");
        }
    }

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
