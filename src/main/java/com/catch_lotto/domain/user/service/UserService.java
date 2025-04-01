package com.catch_lotto.domain.user.service;

import com.catch_lotto.domain.user.dto.UserRegisterRequest;
import com.catch_lotto.domain.user.entity.User;
import com.catch_lotto.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        userRepository.save(request.toEntity());
    }
}
