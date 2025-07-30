package com.example.order.domain.order.controller.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MessageUpdateRequest {
  private Long orderId;
  private List<OrderItemRequest> orderItemRequests;

}
