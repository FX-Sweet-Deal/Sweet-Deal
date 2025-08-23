package com.example.store.domain.store.converter;

import com.example.global.anntation.Converter;
import com.example.store.domain.store.controller.model.response.NearbyStoreResponse;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoreNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.example.store.domain.store.controller.model.response.StoreSimpleResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class StoreConverter {

  public Store toEntity(StoreRegisterRequest storeRegisterRequest) {
    Store store = Store.builder()
        .name(storeRegisterRequest.getName())
        .address(storeRegisterRequest.getAddress())
        .phone(storeRegisterRequest.getPhone())
        .businessNumber(storeRegisterRequest.getBusinessNumber())
        .category(storeRegisterRequest.getCategory())
        .operatingTime(storeRegisterRequest.getOperatingTime())
        .build();

    if (store.isOperating()) {
      store.setOperatingStatus(OperatingStatus.OPEN);
    }
    return store;
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
        .openingTime(store.getOperatingTime().getOpeningTime())
        .closingTime(store.getOperatingTime().getClosingTime())
        .operatingStatus(store.getOperatingStatus())
        .build();

  }

  public List<NearbyStoreResponse> toNearbyStoreResponse(List<Store> stores, BigDecimal radiusInMeters) {
    return stores.stream().map(
        store -> NearbyStoreResponse.builder()
            .id(store.getId())
            .sido(store.getAddress().getSido())
            .sigungu(store.getAddress().getSigungu())
            .eupMyeonDong(store.getAddress().getSigungu())
            .latitude((store.getAddress().getLatitude()))
            .longitude(store.getAddress().getLongitude())
            .radiusInMeters(radiusInMeters)
            .build()
    ).collect(Collectors.toList());

  }

  public StoreSimpleResponse toStoreSimpleResponse(List<Store> stores) {
    List<Long> storeId = stores.stream().map(store -> store.getId()).toList();

    StoreSimpleResponse storeSimpleResponse = new StoreSimpleResponse();
    storeSimpleResponse.setStoresId(storeId);

    return storeSimpleResponse;
  }

  public List<StoreNameKeywordResponse> toStoreNameKeywordResponse(List<Store> stores) {
    return stores.stream().map(
        store -> StoreNameKeywordResponse.builder()
            .storeId(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .phone(store.getPhone())
            .operatingStatus(store.getOperatingStatus())
            .operatingTime(store.getOperatingTime())
            .build()
    ).collect(Collectors.toList());

  }

  public List<OwnerStoresResponse> toOwnerStoresResponse(List<Store> stores) {
    return stores.stream().map(
        store -> OwnerStoresResponse.builder()
            .storeId(store.getId())
            .name(store.getName())
            .address(store.getAddress())
            .phone(store.getPhone())
            .businessNumber(store.getBusinessNumber())
            .category(store.getCategory())
            .status(store.getStoreStatus())
            .registeredAt(store.getRegisteredAt())
            .build()
    ).collect(Collectors.toList());

  }

  public UserStoreResponse toUserStoreResponse(Store store) {
    return UserStoreResponse.builder()
        .storeId(store.getId())
        .name(store.getName())
        .address(store.getAddress())
        .phone(store.getPhone())
        .businessNumber(store.getBusinessNumber())
        .category(store.getCategory())
        .operatingStatus(store.getOperatingStatus())
        .operatingTime(store.getOperatingTime())
        .build();
  }
}
