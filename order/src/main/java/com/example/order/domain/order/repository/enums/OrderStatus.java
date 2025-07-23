package com.example.order.domain.order.repository.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
  NEW("주문 생성"),
  REGISTERED("주문 완료"),
  PAID("결제 완료"),
  CANCELLED("주문 취소");

  private final String description;

}
