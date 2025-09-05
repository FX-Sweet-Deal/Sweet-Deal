package com.example.item.domain.common.exception;

import com.example.global.api.Api;
import com.example.global.errorCode.ItemErrorCode;
import com.example.item.domain.common.exception.item.ItemAlreadyExistsException;
import com.example.item.domain.common.exception.item.ItemCannotDeleteException;
import com.example.item.domain.common.exception.item.ItemNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ItemExceptionHandler {

  @ExceptionHandler(value = ItemNotFoundException.class)
  public ResponseEntity<Api<Object>> itemNotFoundException(ItemNotFoundException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Api.error(ItemErrorCode.ITEM_NOT_FOUND));
  }

  @ExceptionHandler(value = ItemAlreadyExistsException.class)
  public ResponseEntity<Api<Object>> itemAlreadyExistsException(ItemAlreadyExistsException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Api.error(ItemErrorCode.ITEM_ALREADY_EXISTS));
  }

  @ExceptionHandler(value = ItemCannotDeleteException.class)
  public ResponseEntity<Api<Object>> itemCannotDeleteException(ItemCannotDeleteException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Api.error(ItemErrorCode.ITEM_ALREADY_DELETED));
  }



}
