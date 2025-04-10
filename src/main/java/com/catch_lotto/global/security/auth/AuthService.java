package com.catch_lotto.global.security.auth;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.service.CustomUserDetailsService;
import com.catch_lotto.global.exception.CustomException;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import com.catch_lotto.global.response.ResponseUtil;
import com.catch_lotto.global.security.jwt.util.JwtUtil;
import com.catch_lotto.global.security.jwt.TokenResponse;
import com.catch_lotto.global.util.RedisUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public ApiResponse<String> reissue(HttpServletResponse response, String refreshToken) {
        // 1. Refresh Token 검증
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            throw new CustomException(ResponseCode.INVALID_TOKEN);
        }

        // 2. 사용자 ID 추출
        String username = jwtUtil.getSubject(refreshToken);

        UserDetails customUserDetails = customUserDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.createJwtAccessToken((CustomUserDetails)customUserDetails);
        String newRefreshToken = jwtUtil.createJwtRefreshToken((CustomUserDetails)customUserDetails);

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        response.addCookie(jwtUtil.createCookie(newRefreshToken));

        return ApiResponse.success(ResponseCode.SUCCESS_REISSUE, newAccessToken);
    }

}