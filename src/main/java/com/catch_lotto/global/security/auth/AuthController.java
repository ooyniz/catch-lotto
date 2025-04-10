package com.catch_lotto.global.security.auth;

import com.catch_lotto.global.exception.CustomException;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
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
    public ApiResponse<String> reissue(HttpServletResponse response, @CookieValue(name = "refreshToken") String refreshToken) {
        return authService.reissue(response, refreshToken);
    }
}
