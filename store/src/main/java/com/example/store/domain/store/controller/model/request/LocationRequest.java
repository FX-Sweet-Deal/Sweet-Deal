package com.example.store.domain.store.controller.model.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationRequest {

  private String sido;
  private String sigungu;
  private String eupMyeonDong;
  private BigDecimal latitude;
  private BigDecimal longitude;
  private BigDecimal radiusInMeters;

}
