package com.example.item.domain.item.repository;

import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.item.InsufficientItemQuantityException;
import com.example.item.domain.common.exception.item.InvalidExpiredAtException;
import com.example.item.domain.common.exception.item.InvalidItemNameException;
import com.example.item.domain.common.exception.item.InvalidItemPriceException;
import com.example.item.domain.common.exception.item.InvalidItemStatusChangeException;
import com.example.item.domain.common.exception.item.InvalidQuantityException;
import com.example.item.domain.item.repository.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "item_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Long quantity;

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

    public void unregister(Long quantity) {
        this.status = ItemStatus.DELETED;
        this.quantity = quantity;
        this.unregisteredAt = LocalDateTime.now();

    }

    public void changeStatus(ItemStatus status) {
        this.status = status;
    }
    public void updateQuantity(Long quantity) {
        if(quantity <= 0) {
            throw new InvalidQuantityException(ItemErrorCode.INVALID_ITEM_QUANTITY);
        }
        this.quantity = quantity;
    }

    public void rename(String name) {
        if(name == null || name.isEmpty()) {
            throw new InvalidItemNameException(ItemErrorCode.INVALID_ITEM_NAME);
        }

        this.name = name;
    }

    public void updateExpiredAt(LocalDateTime expiredAt) {
        LocalDateTime present = LocalDateTime.now();

        if(expiredAt.isBefore(present)) {
            throw new InvalidExpiredAtException(ItemErrorCode.INVALID_ITEM_EXPIRED_DATE);
        }

        this.expiredAt = expiredAt;
    }

    public void updatePrice(Long price) {
        if(price < 0) {
            throw new InvalidItemPriceException(ItemErrorCode.INVALID_ITEM_PRICE);
        }

        this.price = price;
    }

    public void updateStatus(ItemStatus status) {
        if(status == ItemStatus.DELETED || status == ItemStatus.SOLD) {
            throw new InvalidItemStatusChangeException(ItemErrorCode.INVALID_ITEM_STATUS_CHANGE);
        }

        this.status = status;
    }

    public Long remainingQuantity(Long quantity) {
        if(this.quantity < quantity) {
            throw new InsufficientItemQuantityException(ItemErrorCode.INSUFFICIENT_ITEM_QUANTITY);
        }
        return this.quantity - quantity;
    }

    public void decreaseStock(Long quantity) {
       this.quantity -= quantity;
    }

    public void cancelStock(Long quantity) {
        this.quantity += quantity;
    }

    public void setOrder(Long orderId) {
        this.orderId = orderId;
    }

}
