package com.catch_lotto.global.security.auth;

import com.catch_lotto.global.security.jwt.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletResponse response, @CookieValue(name = "refreshToken") String refreshToken) {
        TokenResponse tokenResponse = authService.reissue(response, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}
