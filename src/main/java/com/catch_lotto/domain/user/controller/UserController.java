package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.UserRegisterRequest;
import com.catch_lotto.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterRequest request) {
        userService.register(request);
    }

}
