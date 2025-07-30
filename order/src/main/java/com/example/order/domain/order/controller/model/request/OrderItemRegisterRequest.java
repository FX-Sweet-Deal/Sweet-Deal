package com.example.order.domain.order.controller.model.request;

import com.example.order.domain.order.repository.OrderItem;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderItemRegisterRequest {

  private List<OrderItemRequest> orderItems;

}
