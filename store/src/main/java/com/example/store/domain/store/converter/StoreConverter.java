package com.example.store.domain.store.converter;

import com.example.global.anntation.Converter;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.example.store.domain.store.repository.Store;

@Converter
public class StoreConverter {

  public Store toEntity(StoreRegisterRequest storeRegisterRequest) {
    return Store.builder()
        .name(storeRegisterRequest.getName())
        .address(storeRegisterRequest.getAddress())
        .phone(storeRegisterRequest.getPhone())
        .category(storeRegisterRequest.getCategory())
        .build();

  }

  public StoreRegisterResponse toResponse(Store store) {
    return StoreRegisterResponse.builder()
        .id(store.getId())
        .name(store.getName())
        .address(store.getAddress())
        .phone(store.getPhone())
        .businessNumber(store.getBusinessNumber())
        .category(store.getCategory())
        .storeStatus(store.getStoreStatus())
        .registeredAt(store.getRegisteredAt())
        .build();

  }

}
