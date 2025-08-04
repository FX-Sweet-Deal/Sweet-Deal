package com.example.store.domain.store.repository.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum StoreStatus {

  REGISTERED("등록"),
  UNREGISTERED("탈퇴");

  private String description;
}
