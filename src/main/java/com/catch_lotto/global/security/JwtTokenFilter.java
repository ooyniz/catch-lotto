package com.catch_lotto.global.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

//@Component
//@RequiredArgsConstructor
//public class JwtTokenFilter implements Filter {
//    private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//        String token = httpServletRequest.getHeader("Authorization");
//
//        String path = httpServletRequest.getRequestURI();
//        // 로그인, 회원가입, 루트 경로는 토큰 검증 없이 통과
//        if (path.equals("/")) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            if (token != null && token.startsWith("Bearer ")) {
//                String jwtToken = token.substring(7);
//
//                // 토큰 검증 및 파싱
//                Claims claims = jwtTokenProvider.validateAndGetClaims(jwtToken);
//                if (claims != null) {
//                    String role = claims.get("role", String.class);
//                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
//                    UserDetails userDetails = new User(claims.getSubject(), "", authorities);
//                    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, jwtToken, authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//            chain.doFilter(request, response);
//        } catch (AuthenticationServiceException e) {
//            logger.error("토큰 인증 오류: {}", e.getMessage());
//            sendErrorResponse(httpServletResponse, "Unauthorized: Invalid token");
//        } catch (Exception e) {
//            logger.error("예상치 못한 오류 발생", e);
//            sendErrorResponse(httpServletResponse, "Unauthorized: Authentication error");
//        }
//    }
//
//    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"" + message + "\"}");
//    }
//}
