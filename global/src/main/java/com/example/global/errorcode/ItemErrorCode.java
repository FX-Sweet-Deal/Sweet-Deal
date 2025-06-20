package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode implements ErrorCode {
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1100, "상품을 찾을 수 없습니다."),
    INVALID_ITEM_INFO(HttpStatus.BAD_REQUEST.value(), 1101, "상품 정보가 유효하지 않습니다."),
    ITEM_UPDATE_DENIED(HttpStatus.FORBIDDEN.value(), 1102, "상품 수정 권한이 없습니다."),
    FAIL_CREATE_ITEM(HttpStatus.INTERNAL_SERVER_ERROR.value(),1103, "상품 등록에 실패했습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;

}
