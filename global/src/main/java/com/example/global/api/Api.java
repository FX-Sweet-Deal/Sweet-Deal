package com.example.global.api;

import com.example.global.errorcode.ErrorCode;

public record Api<T>(Integer errorCode, String description, T result) {

  public static <T> Api<T> ok(T result) {
    return new Api<>(200, "OK", result);
  }

  public static <T> Api<T> error(ErrorCode errorCode) {
    return new Api<>(errorCode.getErrorCode(), errorCode.getDescription(), null);
  }



}
