package com.example.store.domain.store.controller.model.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyStoreResponse {
  private Long id;
  private String sido;
  private String sigungu;
  private String eupMyeonDong;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private BigDecimal radiusInMeters;

}
