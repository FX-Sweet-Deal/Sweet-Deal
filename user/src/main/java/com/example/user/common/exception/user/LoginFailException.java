package com.example.user.common.exception.user;

import com.example.global.errorcode.ErrorCode;

public class LoginFailException extends RuntimeException {

    private final ErrorCode errorCodeIfs;
    private final String description;

    public LoginFailException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public LoginFailException(ErrorCode errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public LoginFailException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public LoginFailException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

}
