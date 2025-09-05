package com.example.image.domain.image.repository.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ImageKind {
  ITEM("상품 이미지"),
  STORE("스토어 이미지");

  private String description;
}
