package com.example.item.domain.common.exception.item;

import com.example.global.errorCode.ErrorCode;

public class InvalidItemInformationException extends RuntimeException {

    private ErrorCode errorCode;
    private String description;

    public InvalidItemInformationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }

}
