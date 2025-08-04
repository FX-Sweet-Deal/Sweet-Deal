package com.example.store.domain.store.repository.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum OperatingStatus {
  OPEN("영업 중"),
  CLOSED("영업 종료"),
  DAY_OFF("휴무");

 private String description;

}
