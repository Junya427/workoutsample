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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.workoutsample.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * アプリケーションのセキュリティ設定を行うクラスです。
 * ユーザー認証、認可、ログイン・ログアウトの設定を定義します。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Profile("!dev")
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * コンストラクタ。
     * カスタムの {@link CustomUserDetailsService} を注入します。
     *
     * @param customUserDetailsService カスタムのユーザーデータサービス
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * パスワードエンコーダーを設定します。
     * BCryptアルゴリズムを使用します。
     *
     * @return パスワードエンコーダー
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * セキュリティフィルタチェーンを構成します。
     * アクセス制御、ログイン、ログアウト、ユーザー認証の設定を定義します。
     *
     * @param http {@link HttpSecurity} オブジェクト
     * @return 構成済みの {@link SecurityFilterChain}
     * @throws Exception セキュリティ設定の構築中にエラーが発生した場合
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(customUserDetailsService)
            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .logoutSuccessUrl("/logout-success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/style.css", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/login", "/logout-success", "/api/**").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            );
        return http.build();
    }

    /**
     * カスタムの認証成功ハンドラを提供します。
     * ユーザーの権限に基づいてリダイレクト先を決定します。
     *
     * @return {@link AuthenticationSuccessHandler} オブジェクト
     */
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException {
                String redirectUrl = null;

                // 管理者権限がある場合のリダイレクト先
                if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                    redirectUrl = "/admin/users";
                } 
                // ユーザー権限がある場合のリダイレクト先
                else if (authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {
                    redirectUrl = "/user/sessions";
                }

                // ログでリダイレクトURLを確認
                logger.info("Redirecting to: {}", redirectUrl);

                // デフォルトのリダイレクト先を設定
                if (redirectUrl == null) {
                    redirectUrl = "/default";
                }

                response.sendRedirect(redirectUrl);
            }
        };
    }
}
