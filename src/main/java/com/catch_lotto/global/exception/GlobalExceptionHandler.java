package com.catch_lotto.global.exception;

import com.catch_lotto.global.response.ApiResponse;
import com.catch_lotto.global.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        log.warn("CustomException 발생: {}", ex.getResponseCode().getMessage());
        ResponseCode responseCode = ex.getResponseCode();
        return ResponseEntity
                .status(responseCode.getStatus())
                .body(ApiResponse.error(responseCode));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception ex) {
        log.error("서버 에러 발생", ex); // 스택트레이스 전체 출력
        return ResponseEntity
                .status(ResponseCode.INTERNAL_ERROR.getStatus())
                .body(ApiResponse.error(ResponseCode.INTERNAL_ERROR));
    }
}