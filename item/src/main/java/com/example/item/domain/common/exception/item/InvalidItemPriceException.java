package com.example.item.domain.common.exception.item;

import com.example.global.errorcode.ErrorCode;

public class InvalidItemPriceException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String description;

    public InvalidItemPriceException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.description = errorCode.getDescription();
    }


}
