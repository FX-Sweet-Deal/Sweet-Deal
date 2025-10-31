package com.example.item.domain.common.exception.item;

import com.example.global.errorcode.ErrorCode;

public class StoreIdRequiredException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public StoreIdRequiredException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
