package com.example.store.domain.store.repository;

import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

  Optional<Store> findFirstByUserIdAndStoreStatusNot(Long userId, StoreStatus storeStatus);
  Optional<Store> findFirstByUserIdAndStoreStatus(Long userId, StoreStatus storeStatus);
  List<Store> findListByUserIdAndStoreStatusNot(Long userId, StoreStatus storeStatus);
  Optional<Store> findByIdAndUserIdAndStoreStatus(Long storeId, Long userId, StoreStatus storeStatus);
  Optional<Store> findByIdOrderByIdDesc(Long id);

  @Query(value = "SELECT * " +
          "FROM store s " +
          "WHERE s.sido = :sido " +
          "AND s.sigungu = :sigungu " +
          "AND s.eup_myeon_dong = :eupMyeonDong " +
          "AND ST_Distance_Sphere(POINT(:lng, :lat), POINT(s.longitude, s.latitude)) <= :radius",
      nativeQuery = true)
  List<Store> findByRegionAndRadius(
      @Param("sido") String sido,
      @Param("sigungu") String sigungu,
      @Param("eupMyeonDong") String eupMyeonDong,
      @Param("lat") BigDecimal latitude,
      @Param("lng") BigDecimal longitude,
      @Param("radius") BigDecimal radiusInMeters
  );
}

