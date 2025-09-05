package com.example.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
     /*
    수정 필요
     */

    OK(HttpStatus.OK.value(), 200, "성공"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), 400, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "서버 내부에 에러가 발생했습니다."),
    NULL_POINT(HttpStatus.INTERNAL_SERVER_ERROR.value(), 512, "NULL POINT 입니다."),
    INVALID_INPUT_DATA(HttpStatus.BAD_REQUEST.value(), 400, "유효하지 않은 입력입니다."),
    MISSING_REQUIRED_HEADER(HttpStatus.BAD_REQUEST.value(), 431, "필수 헤더값이 누락 또는 만료되었습니다."),
    MISSING_REQUIRED_PARAM(HttpStatus.BAD_REQUEST.value(), 431, "필수 파라미터값이 누락되었습니다."),
    ASYNC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "비동기 처리 중 에러가 발생했습니다."),
    DB_PROCESS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 500, "DB 처리 중 에러가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), 401, "접근 권한이 없습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;





}
