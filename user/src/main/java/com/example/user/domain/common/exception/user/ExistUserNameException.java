package com.example.user.domain.common.exception.user;

import com.example.global.errorcode.ErrorCode;

public class ExistUserNameException extends RuntimeException {

    private final ErrorCode errorCodeIfs;
    private final String description;

    public ExistUserNameException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public ExistUserNameException(ErrorCode errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public ExistUserNameException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public ExistUserNameException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

}
