package com.example.store.domain.store.service;

import com.example.store.domain.store.controller.model.request.RegisterImageRequest;
import com.example.store.domain.store.controller.model.request.UpdateImageRequest;
import com.example.store.domain.store.repository.Store;
import com.example.store.domain.store.repository.StoreRepository;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

  private final StoreRepository storeRepository;
  private static final String REGISTER_TOPIC = "image.register";
  private static final String UPDATE_TOPIC = "image.update";
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void save(Store store) {
    storeRepository.save(store);
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

  public List<Store> getStoresByNameKeyword(String name) {
    return storeRepository.findStoresByName(name);
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

  public List<Store> getStoreByDateTime(LocalTime now) {
    return storeRepository.findStoreByDateTimeNow(now);
  }

  public void publishRegisterImage(RegisterImageRequest req) {
    kafkaTemplate
        .send(REGISTER_TOPIC, req.getStoreId().toString(), req)
        .whenComplete((result, ex) -> {
          if(ex != null) {
            log.error("Kafka 발행 실패 (cancel): {}", ex.getMessage(), ex);
          } else {
            log.info("Message sent successfully: {}, topic: {}, partition: {}",
                req.getItemId(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition());
          }
        });

  }

  public void publishUpdateImage(UpdateImageRequest req) {
    kafkaTemplate
        .send(REGISTER_TOPIC, req.getStoreId().toString(), req)
        .whenComplete((result, ex) -> {
          if(ex != null) {
            log.error("Kafka 발행 실패 (cancel): {}", ex.getMessage(), ex);
          } else {
            log.info("Message sent successfully: {}, topic: {}, partition: {}",
                req.getItemId(),
                result.getRecordMetadata().topic(),
                result.getRecordMetadata().partition());
          }
        });

  }




}
