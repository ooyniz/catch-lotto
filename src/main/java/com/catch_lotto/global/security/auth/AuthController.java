package com.catch_lotto.global.security.auth;

import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import com.catch_lotto.global.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<?>> validate(@RequestHeader("Authorization") String requestAccessToken) {
        boolean isValidate = authService.validate(requestAccessToken);

        ResponseCode responseCode = isValidate ? ResponseCode.VALID_TOKEN : ResponseCode.INVALID_TOKEN;
        return ResponseEntity.status(responseCode.getStatus())
                .body(ApiResponse.success(responseCode, isValidate));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response, @CookieValue(name = "refreshToken") String refreshToken) {
        String newAccessToken = authService.reissue(request, response, refreshToken);

        return ResponseEntity.status(ResponseCode.SUCCESS_REISSUE.getStatus())
                .body(ApiResponse.success(ResponseCode.SUCCESS_REISSUE, newAccessToken));
    }
}
