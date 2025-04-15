package com.catch_lotto.global.response;

import com.catch_lotto.domain.lotto.dto.LottoStat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(ResponseCode responseCode, T data) {
        return new ApiResponse<>(responseCode.getStatus().value(), responseCode.getMessage(), data);
    }

    public static ApiResponse<Void> success(ResponseCode responseCode) {
        return new ApiResponse<>(responseCode.getStatus().value(), responseCode.getMessage(), null);
    }

    public static ApiResponse<Void> error(ResponseCode responseCode) {
        return new ApiResponse<>(responseCode.getStatus().value(), responseCode.getMessage(), null);
    }
}
