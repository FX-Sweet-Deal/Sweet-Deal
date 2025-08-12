package com.example.store.domain.common.exception.ntsBusiness;

import com.example.global.errorcode.ErrorCode;

public class InternalException extends OdcloudApiException {
    public InternalException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InternalException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

}
