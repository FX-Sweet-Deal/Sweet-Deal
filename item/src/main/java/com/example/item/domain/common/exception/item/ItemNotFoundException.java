package com.example.item.domain.common.exception.item;

import com.example.global.errorCode.ErrorCode;

public class ItemNotFoundException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public ItemNotFoundException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
