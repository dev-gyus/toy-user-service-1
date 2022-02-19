package com.example.toyuserservice.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /** 400 BAD_REQUEST **/
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다."),
    DUPLICATED_EMAIL(BAD_REQUEST, "중복된 이메일입니다. 확인 후 다시 요청 해 주세요."),
    DUPLICATED_NICKNAME(BAD_REQUEST, "중복된 닉네임입니다.. 확인 후 다시 요청 해 주세요."),

    /** 404 NOT_FOUND **/
    MEMBER_NOT_FOUND(NOT_FOUND, "유저를 찾을 수 없습니다. 확인 후 다시 요청 해 주세요."),


    /** 500 SERVER_ERROR **/
    GENERIC_INSTANTIATING_FAILED(INTERNAL_SERVER_ERROR, "데이터 바인딩시 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
