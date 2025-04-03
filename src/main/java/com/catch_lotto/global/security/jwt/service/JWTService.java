package com.catch_lotto.global.security.jwt.service;

import com.catch_lotto.global.security.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private final JWTUtil jwtUtil;

    public JWTService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = extractRefreshTokenFromCookies(request);
        if (refreshToken == null) {
            return createErrorResponse("Refresh token not found", HttpStatus.UNAUTHORIZED);
        }

        try {
            // 토큰 검증
            if (jwtUtil.isExpired(refreshToken)) {
                return createErrorResponse("Refresh token expired", HttpStatus.UNAUTHORIZED);
            }

            String category = jwtUtil.getCategory(refreshToken);
            if (!"refresh".equals(category)) {
                return createErrorResponse("Invalid refresh token", HttpStatus.UNAUTHORIZED);
            }

            String username = jwtUtil.getUsername(refreshToken);
            String role = jwtUtil.getRole(refreshToken);

            // 새 토큰 생성
            String newAccessToken = jwtUtil.createJWT("access", username, role);
            String newRefreshToken = jwtUtil.createJWT("refresh", username, role);

            response.setHeader("Authorization", "Bearer " + newAccessToken);
            response.addCookie(createCookie(newRefreshToken));
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Access token reissued successfully");
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        } catch (ExpiredJwtException e) {
            return createErrorResponse("Refresh token expired", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return createErrorResponse("Error processing refresh token: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return new ResponseEntity<>(errorResponse, status);
    }

    // todo : component 화
    private Cookie createCookie(String value) {
        Cookie cookie = new Cookie("refresh", value);
        cookie.setMaxAge(24*60*60);
        // https 설정 시
//        cookie.setSecure(true);
//        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

}
