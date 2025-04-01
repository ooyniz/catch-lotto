package com.catch_lotto.domain.user.dto;

import com.catch_lotto.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class UserRegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .nickname(this.nickname)
                .password(this.password)
                .build();
    }
}
