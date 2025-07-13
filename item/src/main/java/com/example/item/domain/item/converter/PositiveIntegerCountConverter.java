package com.example.item.domain.item.converter;

import com.example.global.domain.PositiveIntegerCount;
import jakarta.persistence.AttributeConverter;

public class PositiveIntegerCountConverter implements AttributeConverter<PositiveIntegerCount, Integer> {

  // 엔티티 → DB 저장 값 변환
  @Override
  public Integer convertToDatabaseColumn(PositiveIntegerCount quantity) {
    return (quantity == null ? null : quantity.getCount());
  }

  // DB 조회 값 → 엔티티 VO 변환
  @Override
  public PositiveIntegerCount convertToEntityAttribute(Integer dbData) {
    return (dbData == null ? null : new PositiveIntegerCount(dbData));
  }
}