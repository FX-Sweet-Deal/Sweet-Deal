package com.example.item.domain.item.converter;

import com.example.global.anntation.Converter;
import com.example.item.domain.common.response.MessageResponse;

@Converter
public class MessageConverter {
  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();

  }

}
