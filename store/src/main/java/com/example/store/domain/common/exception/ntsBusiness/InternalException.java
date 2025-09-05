package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorCode.ErrorCode;

public class InternalException extends OpenApiException {
    public InternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
