package com.example.order.domain.order.repository.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
  PENDING_PAYMENT("결제 대기중"),
  PROCESSING("주문 처리중"),
  COMPLETED("주문 완료"),
  CANCELLED("주문 취소");

  private final String description;

}
