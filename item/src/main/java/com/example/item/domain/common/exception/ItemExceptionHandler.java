package com.example.item.domain.common.exception;

import com.example.global.api.Api;
import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.item.InsufficientItemQuantityException;
import com.example.item.domain.common.exception.item.InvalidExpiredAtException;
import com.example.item.domain.common.exception.item.InvalidItemNameException;
import com.example.item.domain.common.exception.item.InvalidItemPriceException;
import com.example.item.domain.common.exception.item.InvalidItemStatusChangeException;
import com.example.item.domain.common.exception.item.InvalidQuantityException;
import com.example.item.domain.common.exception.item.ItemAlreadyExistsException;
import com.example.item.domain.common.exception.item.ItemCannotDeleteException;
import com.example.item.domain.common.exception.item.ItemNotFoundException;
import com.example.item.domain.common.exception.item.StoreIdRequiredException;
import com.example.item.domain.common.exception.item.StoreNotOwnedException;
import com.example.item.domain.common.exception.item.StoreServiceErrorException;
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

  @ExceptionHandler(value = InvalidQuantityException.class)
  public ResponseEntity<Api<Object>> invalidQuantityException(InvalidQuantityException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INVALID_ITEM_QUANTITY));
  }

  @ExceptionHandler(value = InvalidItemNameException.class)
  public ResponseEntity<Api<Object>> invalidItemNameException(InvalidItemNameException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INVALID_ITEM_NAME));
  }

  @ExceptionHandler(value = InvalidExpiredAtException.class)
  public ResponseEntity<Api<Object>> invalidExpireAtException(InvalidExpiredAtException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INVALID_ITEM_EXPIRED_DATE));
  }

  @ExceptionHandler(value = InvalidItemPriceException.class)
  public ResponseEntity<Api<Object>> invalidItemPriceException(InvalidItemPriceException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INVALID_ITEM_PRICE));
  }

  @ExceptionHandler(value = InvalidItemStatusChangeException.class)
  public ResponseEntity<Api<Object>> invalidItemStatusChangeException(InvalidItemStatusChangeException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INVALID_ITEM_STATUS_CHANGE));
  }

  @ExceptionHandler(value = InsufficientItemQuantityException.class)
  public ResponseEntity<Api<Object>> insufficientItemQuantityException(InsufficientItemQuantityException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.INSUFFICIENT_ITEM_QUANTITY));
  }

  @ExceptionHandler(value = StoreServiceErrorException.class)
  public ResponseEntity<Api<Object>> storeServiceErrorException(StoreServiceErrorException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Api.error(ItemErrorCode.STORE_SERVICE_ERROR));
  }

  @ExceptionHandler(value = StoreIdRequiredException.class)
  public ResponseEntity<Api<Object>> storeIdRequiredException(StoreIdRequiredException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Api.error(ItemErrorCode.STORE_ID_REQUIRED));
  }

  @ExceptionHandler(value = StoreNotOwnedException.class)
  public ResponseEntity<Api<Object>> storeNotOwnedException(StoreNotOwnedException e) {
    log.error("", e);
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(Api.error(ItemErrorCode.STORE_NOT_OWNED));
  }



}
