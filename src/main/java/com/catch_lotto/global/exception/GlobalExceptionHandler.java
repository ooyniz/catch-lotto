package com.catch_lotto.global.exception;

import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity
                .status(responseCode.getStatus())
                .body(ApiResponse.error(responseCode));
    }

    // 기본 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        return ResponseEntity
                .status(ResponseCode.INTERNAL_ERROR.getStatus())
                .body(ApiResponse.error(ResponseCode.INTERNAL_ERROR));
    }
}