package com.example.item.domain.common.exception.item;

import com.example.global.errorCode.ErrorCode;

public class ItemCannotDeleteException extends RuntimeException {
    private ErrorCode errorCode;
    private String description;

    public ItemCannotDeleteException(ErrorCode errorCode) {
      super(errorCode.getDescription());
      this.errorCode = errorCode;
      this.description = errorCode.getDescription();
    }
  }

