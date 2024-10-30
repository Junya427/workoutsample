package com.example.workoutsample.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.workoutsample.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!dev")
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class); // ロガーの追加

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .userDetailsService(customUserDetailsService)
        .formLogin(login -> login
            .successHandler(customAuthenticationSuccessHandler()) // ログイン成功時のハンドラー
            .failureUrl("/login?error=true") // ログイン失敗時のリダイレクト先
            .permitAll() // ログインページへのアクセスを許可
        ).logout(logout -> logout
            .logoutUrl("/logout") // ログアウトURL
            .logoutSuccessUrl("/logout-success") // ログアウト成功後のリダイレクト先
            .invalidateHttpSession(true) // セッション無効化
            .deleteCookies("JSESSIONID") // クッキー削除
            .permitAll() // 誰でもアクセス可能にする
        ).authorizeHttpRequests(authz -> authz
            .requestMatchers("/login", "/logout-success", "/api/**").permitAll() // 認証不要のURLを許可
            .requestMatchers("/user/**").hasRole("USER") // ユーザー向けURL
            .requestMatchers("/admin/**").hasRole("ADMIN") // 管理者向けURL
            .anyRequest().authenticated() // それ以外は認証が必要
        );
    return http.build();
}


    private AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException {
                String redirectUrl = null;
    
                if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                    redirectUrl = "/admin/users";
                } else if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
                    redirectUrl = "/user/sessions";
                }
    
                // ログでリダイレクトURLを確認
                logger.info("Redirecting to: {}", redirectUrl);
    
                response.sendRedirect(redirectUrl);
            }
        };
    }
}