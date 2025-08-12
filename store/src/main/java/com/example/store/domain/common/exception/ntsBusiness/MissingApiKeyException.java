package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorcode.ErrorCode;

public class MissingApiKeyException extends OdcloudApiException {

    public MissingApiKeyException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MissingApiKeyException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }


}
