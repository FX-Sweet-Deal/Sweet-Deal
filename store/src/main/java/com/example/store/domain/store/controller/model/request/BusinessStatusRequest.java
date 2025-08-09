package com.example.store.domain.store.controller.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BusinessStatusRequest {
  private List<String> businessNos;
}
