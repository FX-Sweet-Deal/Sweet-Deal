package com.example.store.domain.store.controller.model.response;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OwnerStoresResponse {
  private Long storeId;
  private String name;
  private Address address;
  private String phone;
  private String businessNumber;
  private StoreCategory category;
  private StoreStatus status;
  private LocalDateTime registeredAt;

}
