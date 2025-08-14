package com.example.item.domain.item.repository;

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

    public void unregister() {
        this.status = ItemStatus.DELETED;
        this.unregisteredAt = LocalDateTime.now();
    }

    public void changeStatus(ItemStatus status) {
        this.status = status;
    }
    public void updateQuantity(Long quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("상품 갯수는 1개 이상이어야 합니다."); // 예외 수정 할것
        }
        this.quantity = quantity;
    }

    public void rename(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다."); // 예외 수정 할것
        }

        this.name = name;
    }

    public void updateExpiredAt(LocalDateTime expiredAt) {
        LocalDateTime present = LocalDateTime.now();

        if(expiredAt.isBefore(present)) {
            throw new IllegalArgumentException("유통기한은 현재 날짜 이후여야 합니다."); // 예외 수정 할것
        }

        this.expiredAt = expiredAt;
    }

    public void updatePrice(Long price) {
        if(price < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다."); // 예외 수정 할것
        }

        this.price = price;
    }

    public void updateStatus(ItemStatus status) {
        if(status == ItemStatus.DELETED || status == ItemStatus.SOLD) {
            throw new IllegalArgumentException("삭제되거나 판매된 상품은 상태를 변경할 수 없습니다.");
        }

        this.status = status;
    }

}
