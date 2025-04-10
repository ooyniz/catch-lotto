package com.catch_lotto.domain.user.service;

import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.entity.Role;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.repository.UserRepository;
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

}
