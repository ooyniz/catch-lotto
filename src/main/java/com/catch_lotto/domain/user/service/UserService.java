package com.catch_lotto.domain.user.service;

import com.catch_lotto.domain.user.dto.UserSignupRequest;
import com.catch_lotto.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signup(UserSignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        userRepository.save(request.toEntity());
    }

    public Map<String, Object> checkUsername(String username) {
        boolean exists = userRepository.findByUsername(username).isPresent();

        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        response.put("message", exists ? "이미 사용 중인 아이디입니다." : "사용 가능한 아이디입니다!");

        return response;
    }
}
