package com.example.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Item {
    @Id @GeneratedValue
    @Column(nullable = false, name = "item_id")
    private Long id;

    @Column(nullable = false, length=100)
    private String name;

    private LocalDateTime expired_at;

    private LocalDateTime registered_at;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    /** 전체 10자리 중 2자리 소수점 **/
    @Column(nullable = false, precision = 10, scale = 2)
    private float price;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="order_id")
    private Long orderId;

}
