package com.example.toyuserservice.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /** 400 BAD_REQUEST **/
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),

    /** 404 NOT_FOUND **/
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. 확인 후 다시 요청 해 주세요.");

    private final HttpStatus httpStatus;
    private final String detail;
}
