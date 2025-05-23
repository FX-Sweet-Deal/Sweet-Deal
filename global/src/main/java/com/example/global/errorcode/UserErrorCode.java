package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1000, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.FORBIDDEN.value(), 1001, "이미 존재하는 아이디입니다."),
    DUPLICATE_NICKNAME(HttpStatus.FORBIDDEN.value(), 1002, "이미 존재하는 닉네임입니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED.value(), 1003, "로그인 정보가 일치하지 않습니다."),
    FAIL_WITHDRAWAL(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1004, "회원 탈퇴에 실패했습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;

}