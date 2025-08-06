package com.example.store.domain.store.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Address {

  // 행정구역 (도, 광역시)
  private String sido;

  // 행정구역 (시 군 구)
  private String sigungu;

  // 행정구역 (읍 면 동)
  private String eupMyeonDong;

  // 상세 주소 (도로명, 건물번호 등)
  private String detailAddress;

  // 위도
  @Column(nullable = false, precision = 10, scale = 7)
  private BigDecimal latitude;

  // 경도
  @Column(nullable = false, precision = 10, scale = 7)
  private BigDecimal longitude;

}