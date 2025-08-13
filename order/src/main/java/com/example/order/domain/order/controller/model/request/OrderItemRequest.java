package com.example.order.domain.order.controller.model.request;

import com.example.order.domain.order.repository.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemRequest {
  private Long itemId;
  private Long quantity;


}
