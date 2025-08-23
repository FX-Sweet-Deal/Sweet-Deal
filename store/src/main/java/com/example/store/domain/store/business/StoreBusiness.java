package com.example.store.domain.store.business;

import com.example.global.anntation.Business;
import com.example.global.errorcode.StoreErrorCode;
import com.example.store.domain.common.exception.IsBlankException;
import com.example.store.domain.store.controller.model.response.NearbyStoreResponse;
import com.example.store.domain.store.controller.model.request.LocationRequest;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.MessageResponse;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoreNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.example.store.domain.store.controller.model.response.StoreSimpleResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import com.example.store.domain.store.converter.MessageConverter;
import com.example.store.domain.store.converter.StoreConverter;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreStatus;
import com.example.store.domain.store.service.StoreService;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class StoreBusiness {

  private final StoreService storeService;
  private final StoreConverter storeConverter;
  private final MessageConverter messageConverter;

  public StoreRegisterResponse register(StoreRegisterRequest storeRegisterRequest) {

    // 이게 등록에 있으면 안되지 않을까? 저 공공데이터 API를 사용해서 들어오는 사업자 번호가 유효한지 확인해야 함.
//    if(!storeService.isExistsByBusinessNumber(storeRegisterRequest.getBusinessNumber())) {
//      throw new IllegalArgumentException("사업자 번호가 유효하지 않습니다.");
//    }

    // 임시 userId 삭제 할것 !!!
    Long userId = 0L;

    Store store = storeConverter.toEntity(storeRegisterRequest);

    // 삭제 할 것!!
    store.setUserId(userId);

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
    targetStore.updateBusinessNumber(storeUpdateRequest.getBusinessNumber()); // 유효성 검사를 해야 함...
    targetStore.updateCategory(storeUpdateRequest.getCategory());
    targetStore.updateOperatingTime(storeUpdateRequest.getOperatingTime().getOpeningTime(),
        storeUpdateRequest.getOperatingTime().getClosingTime());

    storeService.save(targetStore);

    return messageConverter.toResponse("스토어 정보가 수정되었습니다.");
  }

  // 점주의 스토어 단건 조회
  public StoreSimpleResponse getSimpleStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);

    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toStoreSimpleResponse(stores);
  }

  // 점주의 스토어 리스트 조회
  public List<OwnerStoresResponse> getOwnerStore(Long userId) {
    List<Store> stores = storeService.getStoresByUserId(userId);
    if(stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toOwnerStoresResponse(stores);
  }

  public Store getStoreByIdAndUserId(Long storeId, Long userId) {
    return storeService.getStoreByIdAndUserId(storeId, userId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));
  }

  // 고객이 조회하는 스토어
  public UserStoreResponse userStore(Long storeId) {
    Store store = storeService.getStoreById(storeId)
        .orElseThrow(() -> new IllegalArgumentException("STORE NOT FOUND"));

    return storeConverter.toUserStoreResponse(store);
  }

  public List<StoreNameKeywordResponse> getStoresByNameKeyword(String name) {
    if (name.isBlank()) {
      throw new IsBlankException(StoreErrorCode.IS_BLANK);

    }

    List<Store> stores = storeService.getStoresByNameKeyword(name);
    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE NOT FOUND");
    }

    return storeConverter.toStoreNameKeywordResponse(stores);

  }

  // 주소 근처 && 영업 중인 스토어
  public List<NearbyStoreResponse> getStoresByNearby(LocationRequest locationRequest) {
    List<Store> nearbyStores = storeService.getStoresByRegionAndRadius(
        locationRequest.getSido(), locationRequest.getSigungu(),
        locationRequest.getEupMyeonDong(), locationRequest.getLatitude(),
        locationRequest.getLongitude(), locationRequest.getRadiusInMeters());

    nearbyStores.stream().filter(
        store -> store.getStoreStatus() == StoreStatus.REGISTERED
        && store.getOperatingStatus() == OperatingStatus.OPEN
    ).collect(Collectors.toList());

    return storeConverter.toNearbyStoreResponse(nearbyStores, locationRequest.getRadiusInMeters());
  }

  public void updateStoreOpenClose() {
    LocalTime now = LocalTime.now();
    List<Store> stores = storeService.getStoreByDateTime(now);

    if (stores.isEmpty()) {
      throw new IllegalArgumentException("STORE_NOT_FOUND");
    }



  }
}
