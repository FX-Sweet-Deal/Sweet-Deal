package com.example.store.domain.store.converter;

import com.example.global.anntation.Converter;
import com.example.store.domain.store.controller.model.response.MessageResponse;

@Converter
public class MessageConverter {
  public MessageResponse toResponse(String message) {
    return MessageResponse.builder()
        .message(message)
        .build();
  }

}
