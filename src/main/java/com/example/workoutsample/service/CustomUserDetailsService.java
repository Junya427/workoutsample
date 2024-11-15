package com.example.workoutsample.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.workoutsample.model.User;
import com.example.workoutsample.repository.AuthorityRepository;
import com.example.workoutsample.repository.UserRepository;

/**
 * Spring Securityの認証に対応するカスタムサービスクラスです。
 * ユーザー情報をロードし、必要な権限情報を含む{@link UserDetails}オブジェクトを提供します。
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 指定されたユーザーネームに基づいて{@link UserDetails}をロードします。
     * 
     * @param username 検索するユーザーネーム
     * @return 指定されたユーザーに対応する{@link UserDetails}オブジェクト
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスローされます
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        Set<GrantedAuthority> grantedAuthorities = authorityRepository.findByUserId(user.getId())
            .stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
            .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            true, true, true, // accountNonExpired, credentialsNonExpired, accountNonLocked
            grantedAuthorities
        );
    }

    /**
     * 指定されたユーザーネームに基づいて独自の{@link User}オブジェクトを検索します。
     * 
     * @param username 検索するユーザーネーム
     * @return 指定されたユーザーに対応する{@link User}オブジェクト
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスローされます
     */
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }
}
