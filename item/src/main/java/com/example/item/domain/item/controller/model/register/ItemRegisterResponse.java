package com.example.item.domain.item.controller.model.register;

import com.example.global.domain.PositiveIntegerCount;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRegisterResponse {

  private Long id;
  private String name;
  private PositiveIntegerCount quantity;
  private LocalDateTime expiredAt;
  private LocalDateTime registerAt;
  private LocalDateTime unregisterAt;
  private ItemStatus status;
  private Long price;
  private Long storeId;
  private Long orderId;

  public ItemRegisterResponse(Item item) {
    this.id = item.getId();
    this.name = item.getName();
    this.quantity = item.getQuantity();
    this.expiredAt = item.getExpiredAt();
    this.registerAt = item.getRegisteredAt();
    this.unregisterAt = item.getUnregisteredAt();
    this.status = item.getStatus();
    this.price = item.getPrice();
    this.storeId = item.getStoreId();
    this.orderId = item.getOrderId();

  }

}
