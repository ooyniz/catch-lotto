package com.catch_lotto.global.security.jwt.controller;

import com.catch_lotto.global.security.jwt.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JWTController {

    private final JWTService jwtService;

    public JWTController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return jwtService.reissue(request, response);
    }
}
