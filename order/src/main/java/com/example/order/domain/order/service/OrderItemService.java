package com.example.order.domain.order.service;

import com.example.order.domain.order.controller.model.request.MessageUpdateRequest;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemService {

  private final OrderItemRepository orderItemRepository;
  private final KafkaTemplate<String, MessageUpdateRequest> kafkaTemplate;

  private static final String UPDATE_TOPIC = "item.update";
  private static final String CANCEL_TOPIC = "item.cancel";

  public OrderItem save(OrderItem orderItem) {
    return orderItemRepository.save(orderItem);
  }

  public List<OrderItem> findByOrderId(Long id) {
    return orderItemRepository.findByOrderId(id);
  }


  public void publishUpdateItem(MessageUpdateRequest req) {
    // key로 orderId를 넣으면, 같은 주문 메시지는 같은 파티션으로 보낸다.
    kafkaTemplate
        .send(UPDATE_TOPIC, req.getOrderId().toString(), req)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Kafka 발행 실패 (update): {}", ex.getMessage(), ex);
          } else {
            log.info("Message sent successfully: {} topic: {}, partition: {}",
                req.getOrderId(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition());
          }
        });
  }

  /**
   * 상품 취소 메시지를 Kafka에 발행
   */
  public void publishCancelItem(MessageUpdateRequest req) {
    kafkaTemplate
        .send(CANCEL_TOPIC, req.getOrderId().toString(), req)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Kafka 발행 실패 (cancel): {}", ex.getMessage(), ex);
          } else {
            log.info("Message sent successfully: {} topic: {}, partition: {}",
                req.getOrderId(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition());
          }
        });
  }
}

