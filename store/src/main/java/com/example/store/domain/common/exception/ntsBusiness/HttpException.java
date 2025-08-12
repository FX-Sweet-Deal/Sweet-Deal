package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorcode.ErrorCode;

public class HttpException extends OpenApiException {
    public HttpException(ErrorCode errorCode) {
        super(errorCode);
    }

    public HttpException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
