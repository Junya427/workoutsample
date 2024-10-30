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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

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

    // 新しいメソッド：独自のUserオブジェクトを取得する
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }
}
