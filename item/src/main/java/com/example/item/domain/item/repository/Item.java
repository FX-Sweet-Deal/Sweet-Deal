package com.example.item.domain.item.repository;

import com.example.global.domain.PositiveIntegerCount;
import com.example.item.domain.item.converter.PositiveIntegerCountConverter;
import com.example.item.domain.item.repository.enums.ItemStatus;
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

    @Column(nullable = false, length = 100)
    private String name;

    @Convert(converter = PositiveIntegerCountConverter.class)
    private PositiveIntegerCount quantity;

    @Column(nullable = false, name="expired_at")
    private LocalDateTime expiredAt;

    @Column(name="registered_at")
    private LocalDateTime registeredAt;

    @Column(name="unregistered_at")
    private LocalDateTime unregisteredAt;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(nullable = false)
    private Long price;

    @Column(name="store_id")
    private Long storeId;

    @Column(name="order_id")
    private Long orderId;

    public void register() {
        this.status = ItemStatus.SALE;
        this.registeredAt = LocalDateTime.now();
    }

    public void unregister() {
        this.status = ItemStatus.DELETED;
        this.unregisteredAt = LocalDateTime.now();
    }

    public void changeStatus(ItemStatus status) {
        this.status = status;
    }

    public int quantity() {
        return getQuantity().getCount();
    }

    public void quantityDecrease(int quantity) {
        getQuantity().decrease(quantity);
    }

    public void quantityIncrease(int quantity) {
        getQuantity().increase(quantity);
    }

}
