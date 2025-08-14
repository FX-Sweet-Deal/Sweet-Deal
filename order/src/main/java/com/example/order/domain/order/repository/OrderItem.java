package com.example.order.domain.order.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "orderItem_id")
  private Long id;

  @Column(nullable = false)
  private Long quantity;

  @Column(nullable = false)
  private Long price;

  @Column(nullable = false)
  private Long totalPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  @JsonIgnore // orders 필드 직렬화 방지
  private Orders orderId;

  @Column(name = "item_id")
  private Long itemId;

  // 직렬화 시 Long orderId로 반환
  @JsonProperty("orderId")
  public Long getOrderId() {
    return orderId != null ? orderId.getId() : null;
  }
  public void addOrders(Orders orders) {
    this.orderId = orders;
  }

  @Override
  public String toString() {
    return "OrderItem{" +
        "id=" + id +
        ", quantity=" + quantity +
        ", price=" + price +
        ", totalPrice=" + totalPrice +
        ", orderId=" + orderId +
        ", itemId=" + itemId +
        '}';
  }
}
