package com.catch_lotto.global.security.jwt.filter;

import com.catch_lotto.global.security.jwt.JwtUtil;
import com.catch_lotto.global.util.RedisUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class CustomLogoutFilter extends GenericFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public CustomLogoutFilter(JwtUtil jwtUtil, RedisUtil redisUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        if (!requestUri.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = extractAccessToken(request);
        if (accessToken != null && jwtUtil.validateAccessToken(accessToken)) {
            long remainTime = jwtUtil.getAccessTokenRemainingTime(accessToken);
            redisUtil.save(accessToken, "logout", remainTime, TimeUnit.MILLISECONDS);
            redisUtil.delete(jwtUtil.getSubject(accessToken));
        }

        deleteRefreshTokenCookie(response);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"로그아웃 성공\"}");
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        deleteCookie.setHttpOnly(true);
        response.addCookie(deleteCookie);
    }
}