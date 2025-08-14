package com.example.global.errorcode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public interface ErrorCode{
    public Integer getHttpCode();
    public Integer getErrorCode();
    public String getDescription();

}
