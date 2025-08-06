package com.example.store.domain.store.controller.model.response;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreRegisterResponse {

  private Long id;

  private String name;

  private Address address;

  private String phone;

  private String businessNumber;

  private StoreCategory category;

  private OperatingStatus operatingStatus;

  private StoreStatus storeStatus;

  private LocalDateTime registeredAt;

  private LocalDateTime unregisteredAt;

  private Long userId;

}
