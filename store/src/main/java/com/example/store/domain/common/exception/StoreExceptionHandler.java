package com.example.store.domain.common.exception;

import com.example.global.api.Api;
import com.example.global.errorCode.StoreErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class StoreExceptionHandler {

  @ExceptionHandler(value = IsBlankException.class)
  public ResponseEntity<Api<Object>> invalidOrderException(IsBlankException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(StoreErrorCode.IS_BLANK));
  }
}
