package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1150, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_REQUEST(HttpStatus.BAD_REQUEST.value(), 1151, "주문 생성 요청이 유효하지 않습니다."),
    ORDER_UPDATE_DENIED(HttpStatus.FORBIDDEN.value(), 1152, "주문 수정 권한이 없습니다."),
    FAIL_PROCESS_ORDER(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1153, "주문 처리에 실패했습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String message;
}
