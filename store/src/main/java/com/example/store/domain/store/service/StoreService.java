package com.example.store.domain.store.service;

import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.StoreRepository;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;

  public void save(Store store) {
    storeRepository.save(store);
  }

  public Optional<Store> getStoreByUserId(Long userId) {
    return storeRepository.findFirstByUserIdAndStoreStatus(userId, StoreStatus.REGISTERED);
  }

  public List<Store> getStoresByUserId(Long userId) {
    return storeRepository.findListByUserIdAndStoreStatus(userId, StoreStatus.REGISTERED);
  }

  public Optional<Store> getStoreByIdAndUserId(Long storeId, Long userId) {
    return storeRepository.findByIdAndUserIdAndStoreStatus(storeId, userId, StoreStatus.REGISTERED);
  }

  public Optional<Store> getStoreById(Long storeId) {
    return storeRepository.findByIdOrderByIdDesc(storeId);
  }

  public List<Store> getStoresByRegionAndRadius(String sido, String sigungu, String eupMyeonDong,
      BigDecimal latitude, BigDecimal longitude, BigDecimal radiusInMeters) {
    return storeRepository.findByRegionAndRadius(sido, sigungu, eupMyeonDong, latitude, longitude, radiusInMeters);
  }

  public boolean isExistsByBusinessNumber(String businessNumber) {
    return storeRepository.existsByBusinessNumber(businessNumber);
  }

  public void delete(Store store) {
    storeRepository.save(store);
  }
}
