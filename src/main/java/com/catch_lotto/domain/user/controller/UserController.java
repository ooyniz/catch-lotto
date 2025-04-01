package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.UserRegisterRequest;
import com.catch_lotto.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterRequest request) {
        userService.register(request);
    }
}
