package com.example.item.domain.item.controller.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MessageUpdateRequest {
  private Long orderId;
  private List<OrderItemRequest> orderItemRequests;

}
