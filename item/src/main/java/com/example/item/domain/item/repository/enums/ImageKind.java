package com.example.item.domain.item.repository.enums;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public enum ImageKind {
  ITEM("상품 이미지"),
  STORE("스토어 이미지");

  private String description;
}
