package com.example.order.domain.order.repository;

import com.example.order.domain.order.repository.enums.PaymentMethod;
import com.example.order.domain.order.repository.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long paymentId;

  @Enumerated
  private PaymentStatus paymentStatus;

  @Enumerated
  private PaymentMethod paymentMethod; // 결제 방식

  @Column(name = "payment_at")
  private LocalDateTime paymentAt;

  @Column(name = "pay_price")
  private Long payPrice;

  @OneToOne
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private Orders orderId;

  public static Payment createPayment(PaymentMethod paymentMethod, Long calculatedPrice, Orders orders) {
    return new Payment(null, PaymentStatus.PENDING, paymentMethod, LocalDateTime.now(),
        calculatedPrice, orders);
  }

  public void setPaymentAt() {
    this.paymentAt = LocalDateTime.now();
  }

  public void complete() {
    if(paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalArgumentException("결제 대기중에만 완료할 수 있습니다.");
    }
    this.paymentStatus = PaymentStatus.COMPLETED;

  }

  public void fail() {
    if(paymentStatus != PaymentStatus.PENDING) {
      throw new IllegalArgumentException("결제 대기중에만 실패할 수 있습니다.");
    }
    this.paymentStatus = PaymentStatus.FAILED;
  }

  public void cancel() {
    switch(paymentStatus) {
      case COMPLETED -> paymentStatus = PaymentStatus.REFUNDED;
      case PENDING, FAILED -> paymentStatus = PaymentStatus.CANCELLED;
      case CANCELLED -> throw new IllegalArgumentException("이미 취소가 완료되었습니다.");
      case REFUNDED -> throw new IllegalArgumentException("이미 환불 처리가 완료되었습니다.");
    }
  }

  public Boolean isSuccess() {
    return paymentStatus == PaymentStatus.COMPLETED;
  }


}
