package com.example.store.domain.common.exception;


import com.example.global.errorCode.ErrorCode;

public class IsBlankException extends RuntimeException{

  private ErrorCode errorCode;
  private String description;

  public IsBlankException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.description = errorCode.getDescription();
  }

}
