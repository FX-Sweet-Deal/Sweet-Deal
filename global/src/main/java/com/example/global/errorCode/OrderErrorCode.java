package com.example.global.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements ErrorCode {
    /*
    수정 필요
     */

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), 1150, "주문을 찾을 수 없습니다."),
    INVALID_ORDER_REQUEST(HttpStatus.BAD_REQUEST.value(), 1151, "주문 생성 요청이 유효하지 않습니다."),
    ORDER_UPDATE_DENIED(HttpStatus.FORBIDDEN.value(), 1152, "주문 수정 권한이 없습니다."),
    FAIL_PROCESS_ORDER(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1153, "주문 처리에 실패했습니다."),
    ITEM_NOT_EXIST(HttpStatus.BAD_REQUEST.value(), 1154, "상품이 존재하지 않습니다."),
    ITEM_EXPIRED(HttpStatus.BAD_REQUEST.value(), 1155, "소비기한이 지났습니다."),
    ITEM_COUNT_NOT_ENOUGH(HttpStatus.BAD_REQUEST.value(), 1156, "재고가 부족합니다. 요청하신 수량을 처리할 수 없습니다."),
    STORE_NOT_EXIST(HttpStatus.NOT_FOUND.value(), 1157, "스토어가 존재하지 않습니다.");

    private final Integer httpCode;
    private final Integer errorCode;
    private final String description;
}
