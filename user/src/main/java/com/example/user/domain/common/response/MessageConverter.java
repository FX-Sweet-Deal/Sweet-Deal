package com.example.user.domain.common.response;


import com.example.global.anntation.Converter;

@Converter
public class MessageConverter {


    public MessageResponse toResponse(String message) {
        return MessageResponse.builder()
            .message(message)
            .build();

    }
}
