package com.example.store.domain.store.repository.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum StoreCategory {
  CONVENIENCE_STORE("편의점"),
  SUPERMARKET("슈퍼 마켓"),
  CAFE("카페");

  private String description;
}
