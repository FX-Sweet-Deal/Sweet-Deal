package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    /*
    수정 필요
     */

    IMAGE_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), 1200, "이미지 파일이 존재하지 않습니다."),
    UNSUPPORTED_FORMAT(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), 1201, "지원하지 않는 이미지 형식입니다."),
    IMAGE_TOO_LARGE(HttpStatus.BAD_REQUEST.value(), 1202, "이미지 크기가 너무 큽니다."),
    FAIL_SAVE_IMAGE(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1203, "이미지 저장에 실패했습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;
}