package com.catch_lotto.domain.user.dto;

import com.catch_lotto.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Data
@NoArgsConstructor
public class UserSignupRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birth;

    private Character gender;

    public User toEntity() {
        return User.builder()
                .username(this.username)
                .password(this.password)
                .nickname(this.nickname)
                .birth(this.birth)
                .gender(this.gender)
                .build();
    }
}
