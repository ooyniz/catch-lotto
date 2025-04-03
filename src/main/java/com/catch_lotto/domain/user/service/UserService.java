package com.catch_lotto.domain.user.service;

import com.catch_lotto.domain.user.dto.UserLoginRequest;
import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.entity.Role;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User signup(UserSignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

//        User user = request.toEntity(bCryptPasswordEncoder);
//        userRepository.save(user);
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword) // 🔹 암호화된 비밀번호 저장
                .nickname(request.getNickname())
                .birth(request.getBirth())
                .gender(request.getGender())
                .role(Role.USER) // 기본값 USER
                .build();
                userRepository.save(user);
        return user;
    }

    public Map<String, Object> checkUsername(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다!");

        return response;
    }

}
