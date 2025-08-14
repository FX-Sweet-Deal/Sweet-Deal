package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoreErrorCode implements ErrorCode {
    /*
    수정 필요
     */

    STORE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1050, "스토어를 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), 1051, "스토어 접근 권한이 없습니다."),
    IS_BLANK(HttpStatus.BAD_REQUEST.value(), 1052, "공백은 허용하지 않습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;

}