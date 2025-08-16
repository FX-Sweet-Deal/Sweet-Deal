package com.example.global.api;

import com.example.global.errorcode.ErrorCodeIfs;
import jakarta.validation.Valid;

public record Api<T>(Integer errorCode, String description, @Valid T result) {

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
