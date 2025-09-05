package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorCode.ErrorCode;

public class MissingApiKeyException extends OpenApiException {

    public MissingApiKeyException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MissingApiKeyException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }


}
