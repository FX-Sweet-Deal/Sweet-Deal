package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OpenApiErrorCode implements ErrorCode {


  // 공공데이터 API
  MISSING_API_KEY(HttpStatus.UNAUTHORIZED.value(), 1050, "인증키는 필수 항목 입니다."),
  INVALID_API_KEY(HttpStatus.BAD_REQUEST.value(), 1051, "등록되지 않은 인증키 입니다."),
  TOO_LARGE_REQUEST(HttpStatus.PAYLOAD_TOO_LARGE.value(), 1052, "요청하는 사업자 정보 100개 초과"),
  BAD_JSON_REQUEST(HttpStatus.BAD_REQUEST.value(), 1053, "JSON format 오류"),
  REQUEST_DATA_MALFORMED(HttpStatus.LENGTH_REQUIRED.value(), 1054, "필수 항목 누락"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), 1055, "내부 에러"),
  HTTP_ERROR(HttpStatus.BAD_REQUEST.value(), 1056, "내부 에러");

  private final Integer httpCode;
  private final Integer errorCode;
  private final String description;

}