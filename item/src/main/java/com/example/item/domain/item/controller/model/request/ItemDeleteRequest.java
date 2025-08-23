package com.example.item.domain.item.controller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Jacksonized
public class ItemDeleteRequest {
  private Long itemId;
  private Long quantity;

}
