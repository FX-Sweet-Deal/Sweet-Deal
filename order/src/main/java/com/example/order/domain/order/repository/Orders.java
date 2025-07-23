package com.example.order.domain.order.repository;

import com.example.order.domain.order.repository.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id", nullable = false)
  private Long id;

  @Column(name = "ordered_at")
  private LocalDateTime orderedAt;

  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @Column(name = "total_price", nullable = false)
  private Long totalPrice;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_item", nullable = false)
  private List<OrderItem> orderItem;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "store_id")
  private Long storeId;

}
