package com.catch_lotto.domain.user.controller;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.dto.UserInfoResponse;
import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.dto.UserUpdateRequest;
import com.catch_lotto.domain.user.service.UserService;
import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
                .status(ResponseCode.SUCCESS_SIGNUP.getStatus())
                .body(ApiResponse.success(ResponseCode.SUCCESS_SIGNUP));
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<?>> checkUsername(@RequestParam String username) {
        boolean exists = userService.checkUsername(username);

        ResponseCode responseCode = exists ? ResponseCode.DUPLICATE_USERNAME : ResponseCode.AVAILABLE_USERNAME;
        return ResponseEntity.status(responseCode.getStatus())
                .body(ApiResponse.success(responseCode, exists));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponse response = userService.getMyInfo(userDetails);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_USERINFO.getStatus())
                .body(ApiResponse.success(ResponseCode.SUCCESS_USERINFO, response));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody UserUpdateRequest request) {
        userService.updateUser(userDetails.getUsername(), request);

        return ResponseEntity
                .status(ResponseCode.SUCCESS_UPDATE_USER.getStatus())
                .body(ApiResponse.success(ResponseCode.SUCCESS_UPDATE_USER));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUsername());

        return ResponseEntity
                .status(ResponseCode.SUCCESS_DELETE_USER.getStatus())
                .body(ApiResponse.success(ResponseCode.SUCCESS_DELETE_USER));
    }

}
