package com.example.order.domain.order.controller.model.response;

import com.example.order.domain.order.repository.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItemRegisterResponse {

  private Long id;
  private Long quantity;
  private Long price;
  private Orders orderId;
  private Long itemId;
}
