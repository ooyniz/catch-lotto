package com.catch_lotto.domain.user.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserRegisterRequest {

    private String username;
    private String nickname;
    private String password;
}
