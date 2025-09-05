package com.example.order.domain.common.exception.order;


import com.example.global.errorCode.ErrorCode;

public class InvalidOrderException extends RuntimeException{

  private ErrorCode errorCode;
  private String description;

  public InvalidOrderException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }

}
