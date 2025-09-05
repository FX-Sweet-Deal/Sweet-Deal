package com.example.gateway.common.exception.token;

import com.example.global.errorcode.ErrorCode;


public class TokenExpiredException extends RuntimeException{

    private final ErrorCode errorCodeIfs;
    private final String description;

    public TokenExpiredException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public TokenExpiredException(ErrorCode  errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public TokenExpiredException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public TokenExpiredException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }


}
