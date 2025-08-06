package com.example.store.domain.store.business;

import com.example.global.anntation.Business;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.MessageResponse;
import com.example.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.example.store.domain.store.converter.MessageConverter;
import com.example.store.domain.store.converter.StoreConverter;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.service.StoreService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class StoreBusiness {

  private final StoreService storeService;
  private final StoreConverter storeConverter;
  private final MessageConverter messageConverter;

  public StoreRegisterResponse register(StoreRegisterRequest storeRegisterRequest) {
    if(!storeService.isExistsByBusinessNumber(storeRegisterRequest.getBusinessNumber())) {
      throw new IllegalArgumentException("사업자 번호가 유효하지 않습니다.");
    }

    Store store = storeConverter.toEntity(storeRegisterRequest);
    store.register();
    storeService.save(store);
    return storeConverter.toResponse(store);
  }

  public MessageResponse unregister(Long storeId, Long userId) {
    Store targetStore = storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    targetStore.unregister();
    storeService.delete(targetStore);
    return messageConverter.toResponse("스토어가 삭제되었습니다.");

  }

  public MessageResponse update(Long storeId, StoreUpdateRequest storeUpdateRequest) {
    Store targetStore = storeService.getStoreById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    // 생각 중(수정)
    targetStore.updateName(storeUpdateRequest.getName());
    targetStore.updateAddress(storeUpdateRequest.getAddress());
    targetStore.updatePhone(storeUpdateRequest.getPhone());
    targetStore.updateBusinessNumber(storeUpdateRequest.getBusinessNumber());
    targetStore.updateCategory(storeUpdateRequest.getCategory());

    storeService.save(targetStore);

    return messageConverter.toResponse("스토어가 수정되었습니다.");
  }

  public Store getStoreByUserId(Long userId) {
    return storeService.getStoreByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));
  }

  public List<Store> getStoresByUserId(Long userId) {
    return storeService.getStoresByUserId(Long userId);
  }

  public Store getStoreByIdAndUserId(Long storeId, Long userId) {
    return storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));
  }

  public Store getStoreById(Long storeId) {
    return storeService.getStoreById(Long storeId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));
  }

  public List<Store> getStoresByRegionAndRadius(String sido, String sigungu, String eupMyeonDong,
      BigDecimal latitude, BigDecimal longitude, BigDecimal radiusInMeters) {

    return storeService.getStoresByRegionAndRadius(sido, sigungu, eupMyeonDong,
        latitude, longitude, radiusInMeters);
  }

}
