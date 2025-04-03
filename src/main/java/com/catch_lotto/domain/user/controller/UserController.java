package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody UserSignupRequest request) {
        userService.signup(request);
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Object>> checkUsername(@RequestParam String username) {
        Map<String, Object> response = userService.checkUsername(username);
        return ResponseEntity.ok(response);
    }

}
