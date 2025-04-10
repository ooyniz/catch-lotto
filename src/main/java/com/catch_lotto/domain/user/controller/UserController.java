package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.UserLoginRequest;
import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.service.UserService;
//import com.catch_lotto.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserSignupRequest request) {
        User user = userService.signup(request);
        return new ResponseEntity<>(user.getUserId(), HttpStatus.CREATED);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        Map<String, Object> response = userService.checkUsername(username);
        return ResponseEntity.ok(response);
    }

}
