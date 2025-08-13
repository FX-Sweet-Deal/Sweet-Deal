package com.example.order.domain.order.repository;

import com.example.order.domain.order.repository.enums.OrderStatus;
import com.example.order.domain.order.repository.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long id;

  @Column(name = "ordered_at")
  private LocalDateTime orderedAt;

  @Column(name = "cancelled_at")
  private LocalDateTime cancelledAt;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "total_price", nullable = false)
  private Long totalPrice;

  @Builder.Default
  @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(mappedBy = "orderId", cascade = CascadeType.ALL)
  private Payment payment;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "store_id")
  private Long storeId;

  @JsonProperty("paymentMethod")
  public PaymentMethod getPaymentMethod() {
    return this.payment.getPaymentMethod();
  }

  public void create() {
    this.status = OrderStatus.PENDING_PAYMENT;
    this.orderedAt = LocalDateTime.now();
  }

  public void cancel() {
    this.status = OrderStatus.CANCELLED;
    this.cancelledAt = LocalDateTime.now();
    this.payment.cancel();
  }

  public void complete() {
    this.status = OrderStatus.COMPLETED;
  }

  public Long calculateTotalPrice() {
    return orderItems.stream().mapToLong(orderItem -> {
      return orderItem.getPrice() * orderItem.getQuantity();
    }).sum();
  }

  public boolean isPaymentSuccess() {
    return this.payment.isSuccess();
  }

  // 상품 종류 개수
  public Long countItem() {
    return (long) orderItems.size();
  }

  // 각 상품의 총 개수
  public Long countTotalItemQuantity() {
    return this.orderItems.stream().mapToLong(OrderItem::getQuantity).sum();
  };

  public void changeStatus(OrderStatus orderStatus) {
    this.status = orderStatus;
  }

  public void addOrderItems(OrderItem orderItem) {
    this.orderItems.add(orderItem);
    orderItem.addOrders(this);
  }

  public void initPaymentMethod(PaymentMethod paymentMethod) {
    this.payment = Payment.createPayment(paymentMethod, calculateTotalPrice(), this);

  }


}



