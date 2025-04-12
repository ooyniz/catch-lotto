package com.catch_lotto.global.config;

import com.catch_lotto.global.security.jwt.filter.CustomLogoutFilter;
import com.catch_lotto.global.security.jwt.filter.JwtFilter;
import com.catch_lotto.global.security.jwt.util.CookieUtil;
import com.catch_lotto.global.security.jwt.util.JwtUtil;
import com.catch_lotto.global.security.jwt.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final String[] allowedUrls = {"/", "/reissue", "/login", "/api/user/signup", "/api/user/exists","/error/**"};

    private final JwtFilter jwtFilter;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CustomLogoutFilter customLogoutFilter;
    private final CookieUtil cookieUtil;

    public SecurityConfig(JwtFilter jwtFilter, AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil, CustomLogoutFilter customLogoutFilter, CookieUtil cookieUtil) {
        this.jwtFilter = jwtFilter;
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customLogoutFilter = customLogoutFilter;
        this.cookieUtil = cookieUtil;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 적용
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화 (JWT 사용 시 필요)
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP Basic 인증 비활성화
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안 함
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(allowedUrls).permitAll() // 회원가입, 로그인 API는 허용
//                        .requestMatchers(
//                                "/manifest.json",
//                                "/favicon.ico",
//                                "/**/*.png",
//                                "/**/*.json"
//                        ).permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN") // 관리자 전용 API 보호
//                        .anyRequest().authenticated() // 나머지 모든 요청은 인증 필요
                        .anyRequest().permitAll() // 나머지 모든 요청은 인증 필요
                )
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, LoginFilter.class) // JWT
                .addFilterBefore(customLogoutFilter, LogoutFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // React 프론트엔드 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 명확한 HTTP 메서드 지정
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 인증정보 포함 허용
        configuration.setExposedHeaders(List.of("Authorization")); // JWT 토큰 헤더 노출

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용
        return source;
    }

}