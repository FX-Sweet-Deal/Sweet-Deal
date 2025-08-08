package com.example.store.domain.store.controller.model.response;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.OperatingTime;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreSimpleResponse {
  private Long storeId;

}
