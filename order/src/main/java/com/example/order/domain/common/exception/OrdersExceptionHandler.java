package com.example.order.domain.common.exception;

import com.example.global.api.Api;
import com.example.global.errorcode.OrderErrorCode;
import com.example.order.domain.common.exception.order.ExpiredException;
import com.example.order.domain.common.exception.order.InvalidOrderException;
import com.example.order.domain.common.exception.order.ItemCountNotEnoughException;
import com.example.order.domain.common.exception.order.ItemNotExistFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OrdersExceptionHandler {

  @ExceptionHandler(value = InvalidOrderException.class)
  public ResponseEntity<Api<Object>> invalidOrderException(InvalidOrderException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Api.error(OrderErrorCode.INVALID_ORDER_REQUEST));
  }

  @ExceptionHandler(value = ItemCountNotEnoughException.class)
  public ResponseEntity<Api<Object>> itemCountNotEnoughException(ItemCountNotEnoughException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(OrderErrorCode.ITEM_COUNT_NOT_ENOUGH));
  }

  @ExceptionHandler(value = ItemNotExistFoundException.class)
  public ResponseEntity<Api<Object>> itemNotExistFoundException(ItemNotExistFoundException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(OrderErrorCode.ITEM_NOT_EXIST));
  }

  @ExceptionHandler(value = ExpiredException.class)
  public ResponseEntity<Api<Object>> expiredException(ExpiredException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(OrderErrorCode.ITEM_EXPIRED));
  }

}
