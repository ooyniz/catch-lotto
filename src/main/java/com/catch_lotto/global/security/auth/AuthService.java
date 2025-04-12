package com.catch_lotto.global.security.auth;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.service.CustomUserDetailsService;
import com.catch_lotto.global.exception.CustomException;
import com.catch_lotto.global.response.ResponseCode;
import com.catch_lotto.global.security.jwt.util.CookieUtil;
import com.catch_lotto.global.security.jwt.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(JwtUtil jwtUtil, CookieUtil cookieUtil, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String reissue(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        // 1. Access, Refresh Token 검증
        String accessToken = jwtUtil.resolveAccessToken(request);
        if (accessToken == null || !jwtUtil.validateAccessToken(accessToken)) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        // JWT 자체가 유효한지 확인 todo
        jwtUtil.validateTokenOrThrow(refreshToken);

        // refresh token이 redis에 존재하는지 확인
        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        // 2. 사용자 ID 추출
        String username = jwtUtil.getSubject(refreshToken);
        UserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.createJwtAccessToken((CustomUserDetails)customUserDetails);
        String newRefreshToken = jwtUtil.createJwtRefreshToken((CustomUserDetails)customUserDetails);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(cookieUtil.createCookie(newRefreshToken));

        return newAccessToken;
    }

    public boolean validate(String requestAccessToken) {
        if (requestAccessToken == null) return false;
        else return jwtUtil.validateAccessToken(requestAccessToken);
    }
}