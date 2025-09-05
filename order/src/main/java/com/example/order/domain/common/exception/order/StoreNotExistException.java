package com.example.order.domain.common.exception.order;

import com.example.global.errorCode.ErrorCode;

public class StoreNotExistException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public StoreNotExistException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
