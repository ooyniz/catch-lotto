package com.catch_lotto.global.security.jwt.filter;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.repository.UserRepository;
import com.catch_lotto.global.security.jwt.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    @Autowired
    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.resolveAccessToken(request);

        // accessToken 없이 접근할 경우
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 유효성 검사
        jwtUtil.validateTokenOrThrow(accessToken);

        String username = jwtUtil.getSubject(accessToken);

        User user = userRepository.findByUsername(username).orElseThrow();
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }
}
