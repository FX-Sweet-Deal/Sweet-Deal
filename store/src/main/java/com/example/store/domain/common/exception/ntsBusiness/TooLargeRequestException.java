package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorCode.ErrorCode;

public class TooLargeRequestException extends OpenApiException {

    public TooLargeRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TooLargeRequestException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
