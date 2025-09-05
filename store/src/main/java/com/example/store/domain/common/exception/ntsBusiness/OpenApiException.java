package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorCode.ErrorCode;

public class OpenApiException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String description;

  public OpenApiException(ErrorCode errorCode) {
    super(errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }

  public OpenApiException(ErrorCode errorCode, String message) {
    super(message != null ? message : errorCode.getDescription());
    this.errorCode = errorCode;
    this.description = errorCode.getDescription();
  }

  public ErrorCode getErrorCode() { return errorCode; }
  public String getDescription() { return description; }
}