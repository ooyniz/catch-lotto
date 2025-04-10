package com.catch_lotto.global.security.auth;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.service.CustomUserDetailsService;
import com.catch_lotto.global.security.jwt.JwtUtil;
import com.catch_lotto.global.security.jwt.TokenResponse;
import com.catch_lotto.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(JwtUtil jwtUtil, RedisUtil redisUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    public TokenResponse reissue(HttpServletResponse response, String refreshToken) {
        // 1. Refresh Token 검증
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // 2. 사용자 ID 추출
        String username = jwtUtil.getSubject(refreshToken);

        UserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.createJwtAccessToken((CustomUserDetails)customUserDetails);
        String newRefreshToken = jwtUtil.createJwtRefreshToken((CustomUserDetails)customUserDetails);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(createCookie(newRefreshToken));
        response.setStatus(HttpStatus.OK.value());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refreshToken", value);
        cookie.setMaxAge(24*60*60);
        // https 설정 시
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}