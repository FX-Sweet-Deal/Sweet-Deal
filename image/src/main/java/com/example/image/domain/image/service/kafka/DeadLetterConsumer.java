package com.example.image.domain.image.service.kafka;

import com.example.image.domain.image.controller.model.RegisterImageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadLetterConsumer {

  @KafkaListener(topics = "image.register.DLT", groupId = "image-group-dlt")
  public void listenItemDLT(@Payload RegisterImageRequest registerImageRequest, Exception exception) {
    log.error("Received failed item in DLT: {}, Error: {}",
        registerImageRequest.getItemId(), exception.getMessage());
  }


}
