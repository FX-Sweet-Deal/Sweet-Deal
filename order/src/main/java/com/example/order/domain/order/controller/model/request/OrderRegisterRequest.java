package com.example.order.domain.order.controller.model.request;

import com.example.order.domain.order.repository.enums.PaymentMethod;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderRegisterRequest {

  private Long storeId;
  private List<OrderItemRequest> orderItems;
  private PaymentMethod paymentMethod;

}
