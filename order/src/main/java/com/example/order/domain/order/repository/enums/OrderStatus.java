package com.example.order.domain.order.repository.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
  ORDERED("주문 완료"),
  CANCELED("주문 취소"),
  COMPLETED("결제 완료");

  private final String description;

}
