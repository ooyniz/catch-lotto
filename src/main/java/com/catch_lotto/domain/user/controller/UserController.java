package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.service.UserService;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> signup(@RequestBody UserSignupRequest request) {
        userService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(ResponseCode.SUCCESS_SIGNUP));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<?>> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsername(username);

        ResponseCode code = exists ? ResponseCode.DUPLICATE_USERNAME : ResponseCode.AVAILABLE_USERNAME;
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.success(code, exists));
    }

}
