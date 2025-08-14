package com.example.order.domain.order.controller.model.response;

import com.example.order.domain.order.repository.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderResponse {
  private Long id;
  private LocalDateTime orderedAt;
  private LocalDateTime cancelledAt;
  private OrderStatus status;
  private Long totalPrice;
  private Long userId;
  private Long storeId;
  private List<OrderItemResponse> orderItemResponses;


}
