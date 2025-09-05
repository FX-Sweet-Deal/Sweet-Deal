package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorCode.ErrorCode;

public class BadJsonRequestException extends OpenApiException {

    public BadJsonRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public BadJsonRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
