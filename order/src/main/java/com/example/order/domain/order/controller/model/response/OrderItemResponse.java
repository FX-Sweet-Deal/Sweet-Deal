package com.example.order.domain.order.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
  private Long id;
  private Long quantity;
  private Long price;
  private Long totalPrice;
  private Long itemId;

}
