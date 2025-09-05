package com.example.store.domain.store.repository.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum ImageKind {


  ITEM("상품 이미지"),
  STORE("스토어 이미지");

  private String description;

}
