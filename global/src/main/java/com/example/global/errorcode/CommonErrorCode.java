package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    //== 200 ==//
    SUCCESS(HttpStatus.OK.value(), 200, "OK"),

    //== 400 ==//
    NOT_SUPPORTED_HTTP_METHOD(HttpStatus.BAD_REQUEST.value(),1, "Http Method Failed"),
    NOT_VALID_METHOD_ARGUMENT(HttpStatus.BAD_REQUEST.value(),1, "REQUEST를 확인해주세요"),
    BAD_PASSWORD(HttpStatus.BAD_REQUEST.value(), 1, "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 1, "해당 사용자를 찾을 수 없습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String message;





}
