package com.example.item.domain.item.controller.model.response;

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
  private Long quantity;
  private LocalDateTime expiredAt;
  private Long price;
  private Long storeId;

}
