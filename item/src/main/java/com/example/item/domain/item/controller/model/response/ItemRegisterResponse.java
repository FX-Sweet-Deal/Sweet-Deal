package com.example.item.domain.item.controller.model.response;

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
  private Long quantity;
  private LocalDateTime expiredAt;
  private LocalDateTime registerAt;
  private LocalDateTime unregisterAt;
  private ItemStatus status;
  private Long price;
  private Long storeId;
  private Long orderId;

}
