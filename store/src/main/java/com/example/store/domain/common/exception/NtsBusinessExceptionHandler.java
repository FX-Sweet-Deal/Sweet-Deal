package com.example.store.domain.common.exception;

import com.example.global.errorcode.ErrorCode;
import com.example.store.domain.common.exception.ntsBusiness.OpenApiException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NtsBusinessExceptionHandler {
    @ExceptionHandler(OpenApiException.class)
    public ResponseEntity<?> handleOdcloud(OpenApiException ex) {
      ErrorCode code = ex.getErrorCode();
      return ResponseEntity.status(code.getHttpCode())
          .body(Map.of(
              "code", code.getErrorCode(),
              "message", ex.getMessage()
          ));
    }

    // 요청 바디 검증 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
      var errors = ex.getBindingResult().getFieldErrors().stream()
          .collect(Collectors.toMap(
              fe -> fe.getField(),
              fe -> fe.getDefaultMessage(),
              (a,b) -> a
          ));
      return ResponseEntity.badRequest().body(Map.of(
          "code", 400,
          "message", "잘못된 요청입니다.",
          "errors", errors
      ));
    }
  }
