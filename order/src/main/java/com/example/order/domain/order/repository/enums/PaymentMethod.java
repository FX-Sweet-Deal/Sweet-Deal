package com.example.order.domain.order.repository.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentMethod {

  CREDIT_CARD("신용 카드"),
  DEBIT_CARD("직불 카드"),
  PAYPAL("페이팔"),
  BANK_TRANSFER("계좌 이체"),
  KAKAO_PAY("카카오 페이"),
  TOSS_PAY("토스 페이");

  final String description;

}
