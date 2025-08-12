package com.example.store.domain.store.repository;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenApiErrorBody {
    private Integer code; // -401, -4, -5
    private String msg;
    @JsonProperty("status_code")
    private String statusCode;
    public Integer getCode() { return code; }
    public String getMsg() { return msg; }
    public String getStatusCode() { return statusCode; }
}
