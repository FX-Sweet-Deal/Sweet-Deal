package com.example.item.domain.item.controller.model.detail;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponse {

  private Long id;
  private String name;
  private Integer quantity;
  private LocalDateTime expiredAt;
  private Long price;
  private Long storeId;

}
