package com.catch_lotto.domain.user.service;

import com.catch_lotto.domain.user.dto.CustomUserDetails;
import com.catch_lotto.domain.user.dto.UserInfoResponse;
import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.dto.UserUpdateRequest;
import com.catch_lotto.domain.user.entity.Role;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.repository.UserRepository;
import com.catch_lotto.global.exception.CustomException;
import com.catch_lotto.global.response.ResponseCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void signup(UserSignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .nickname(request.getNickname())
                .birth(request.getBirth())
                .gender(request.getGender())
                .role(Role.USER) // 기본값 USER
                .build();
                userRepository.save(user);
    }

    public boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public UserInfoResponse getMyInfo(CustomUserDetails userDetails) {
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

        return new UserInfoResponse(user.getUsername(), user.getNickname(), user.getBirth(), user.getGender());
    }

    public void updateUser(String username, UserUpdateRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

        user.setNickname(request.getNickname());
        user.setBirth(request.getBirth());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseCode.NOT_FOUND_USER));

        userRepository.delete(user);
    }
}
