package com.example.global.api;

import com.example.global.errorcode.ErrorCodeIfs;

public record Api<T>(Integer errorCode, String description, T result) {

  public static <T> Api<T> ok(T result) {
    return new Api<>(200, "OK", result);
  }

  public static <T> Api<T> error(ErrorCodeIfs errorCode) {
    return new Api<>(errorCode.getErrorCode(), errorCode.getDescription(), null);
  }

  public static <T> Api<T> error(ErrorCodeIfs errorCode, String description) {
    return new Api<>(errorCode.getErrorCode(), description, null);
  }



}
