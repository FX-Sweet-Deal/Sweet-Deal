package com.example.item.domain.common.exception.item;

import com.example.global.errorcode.ErrorCode;

public class ItemAlreadyExistsException extends RuntimeException{
    private ErrorCode errorCode;
    private String description;

    public ItemAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }
}
