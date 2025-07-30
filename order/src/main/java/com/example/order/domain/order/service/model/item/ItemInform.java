package com.example.order.domain.order.service.model.item;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInform {

  private Long id;
  private String name;
  private Long quantity;
  private LocalDateTime expiredAt;
  private LocalDateTime registerAt;
  private ItemStatus status;
  private Long price;
  private Long storeId;

}
