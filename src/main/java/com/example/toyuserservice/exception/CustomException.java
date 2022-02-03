package com.example.toyuserservice.exception;

import com.example.toyuserservice.domain.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
}
