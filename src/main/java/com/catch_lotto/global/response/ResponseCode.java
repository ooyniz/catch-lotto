package com.catch_lotto.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    SUCCESS_SIGNUP(HttpStatus.CREATED, "회원가입에 성공하였습니다."),
    AVAILABLE_USERNAME(HttpStatus.OK, "사용 가능한 아이디입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),
    SUCCESS_LOGIN(HttpStatus.OK, "로그인에 성공하였습니다."),
    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃되었습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류"),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "이미 로그아웃된 상태입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
    SUCCESS_REISSUE(HttpStatus.OK, "재발급 성공"),
    VALID_TOKEN(HttpStatus.OK, "유효한 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰 형식입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    SUCCESS_USERINFO(HttpStatus.OK, "회원 정보를 성공적으로 불러왔습니다."),
    SUCCESS_UPDATE_USER(HttpStatus.OK, "회원 정보를 성공적으로 수정했습니다."),
    SUCCESS_DELETE_USER(HttpStatus.OK, "회원 정보를 성공적으로 삭제했습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    ;

    private final HttpStatus status;
    private final String message;

    ResponseCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}