package com.example.item.domain.item.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class Item {
    @Id @GeneratedValue
    @Column(nullable = false, name = "item_id")
    private Long id;

    @Column(nullable = false, length=100)
    private String name;

    private LocalDateTime expired_at;

    private LocalDateTime registered_at;

    // 추가함. (요구명세서에도 최신화 할 것!.)
    private LocalDateTime unregistered_at;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    private Long price;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="order_id")
    private Long orderId;

}
