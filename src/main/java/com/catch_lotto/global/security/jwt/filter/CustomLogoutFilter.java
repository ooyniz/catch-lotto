package com.catch_lotto.global.security.jwt.filter;

import com.catch_lotto.global.security.jwt.util.CookieUtil;
import com.catch_lotto.global.security.jwt.util.JwtUtil;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.util.RedisUtil;
import com.catch_lotto.global.response.ResponseCode;
import com.catch_lotto.global.response.ResponseUtil;
import jakarta.servlet.*;
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
    private final CookieUtil cookieUtil;

    public CustomLogoutFilter(JwtUtil jwtUtil, RedisUtil redisUtil, CookieUtil cookieUtil) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String requestUri = request.getRequestURI();
        if (!requestUri.equals("/logout")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtUtil.resolveAccessToken(request);
        if ((accessToken == null) || !jwtUtil.validateAccessToken(accessToken)) {
            ApiResponse<?> apiResponse = ApiResponse.error(ResponseCode.INVALID_TOKEN);
            ResponseUtil.writeJson(response, apiResponse);
            return;
        }

        if ("logout".equals(redisUtil.get(accessToken))) {
            ApiResponse<?> apiResponse = ApiResponse.error(ResponseCode.ALREADY_LOGOUT);
            ResponseUtil.writeJson(response, apiResponse);
            return;
        }

        long remainTime = jwtUtil.getAccessTokenRemainingTime(accessToken);
        redisUtil.save(accessToken, "logout", remainTime, TimeUnit.MILLISECONDS);
        redisUtil.delete(jwtUtil.getSubject(accessToken));

        cookieUtil.deleteRefreshTokenCookie(response);
        ApiResponse<?> apiResponse = ApiResponse.success(ResponseCode.SUCCESS_LOGOUT);
        ResponseUtil.writeJson(response, apiResponse);
    }

}