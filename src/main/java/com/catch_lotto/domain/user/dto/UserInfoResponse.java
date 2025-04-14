package com.catch_lotto.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private String nickname;
    private Date birth;
    private Character gender;
}
