package com.example.order.domain.order.controller.model.request;

import com.example.order.domain.order.repository.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
  private boolean success;

}
