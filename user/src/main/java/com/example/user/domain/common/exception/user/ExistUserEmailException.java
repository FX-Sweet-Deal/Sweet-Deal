package com.example.user.domain.common.exception.user;

import com.example.global.errorcode.ErrorCode;
import com.example.global.errorcode.ErrorCodeIfs;

public class ExistUserEmailException extends RuntimeException {

    private final ErrorCode errorCodeIfs;
    private final String description;

    public ExistUserEmailException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public ExistUserEmailException(ErrorCode errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public ExistUserEmailException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public ExistUserEmailException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

}
