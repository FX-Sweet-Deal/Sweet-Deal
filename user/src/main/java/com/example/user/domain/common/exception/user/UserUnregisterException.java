package com.example.user.domain.common.exception.user;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.global.errorcode.ErrorCode;
import com.example.global.errorcode.ErrorCodeIfs;

public class UserUnregisterException extends RuntimeException {

    private final ErrorCode errorCodeIfs;
    private final String description;

    public UserUnregisterException(ErrorCode errorCodeIfs) {
        super(errorCodeIfs.getDescription());
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public UserUnregisterException(ErrorCode errorCodeIfs, String errorDescription) {
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

    public UserUnregisterException(ErrorCode errorCodeIfs, Throwable throwable) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorCodeIfs.getDescription();
    }

    public UserUnregisterException(ErrorCode errorCodeIfs, Throwable throwable,
        String errorDescription) {
        super(throwable);
        this.errorCodeIfs = errorCodeIfs;
        this.description = errorDescription;
    }

}
