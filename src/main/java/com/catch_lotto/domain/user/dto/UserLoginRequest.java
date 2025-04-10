package com.catch_lotto.domain.user.dto;

import com.catch_lotto.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
