package com.example.gateway.common.exception.token;


import com.example.global.errorCode.ErrorCode;


public class NotPermittedException extends RuntimeException {

    private final ErrorCode errorCodeIfs;
    private final String description;

    public NotPermittedException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public NotPermittedException(ErrorCode errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public NotPermittedException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public NotPermittedException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

}
