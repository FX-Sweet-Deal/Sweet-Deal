package com.example.item.domain.item.service.kafka;

import com.example.item.domain.item.controller.model.request.MessageUpdateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeadLetterConsumer {

  @KafkaListener(topics = "item.update.DLT",
  groupId = "item-service-group-dlt")
  public void listenDLT(@Payload MessageUpdateRequest messageUpdateRequest, Exception exception) {
    log.error("Received failed order in DLT: {}, Error: {}",
        messageUpdateRequest.getOrderId(), exception.getMessage());
  }

}
