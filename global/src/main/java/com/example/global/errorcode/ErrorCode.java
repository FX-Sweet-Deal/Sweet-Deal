package com.example.global.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    public Integer getHttpCode();
    public Integer getErrorCode();
    public String getDescription();

}
