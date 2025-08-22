package com.example.store.domain.store.repository;

import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

  // 점장 조회
  Optional<Store> findFirstByUserIdAndStoreStatus(Long userId, StoreStatus storeStatus);
  List<Store> findListByUserIdAndStoreStatus(Long userId, StoreStatus storeStatus);
  Optional<Store> findByIdAndUserIdAndStoreStatus(Long storeId, Long userId, StoreStatus storeStatus);
  Optional<Store> findByIdOrderByIdDesc(Long id);
  Boolean existsByBusinessNumber(String businessNumber);

  @Query(value = """
    SELECT *
    FROM store s
    WHERE s.open_all_day = 1
       OR (
            s.opening_time < s.closing_time
            AND :now >= s.opening_time
            AND :now <  s.closing_time
          )
       OR (
            s.opening_time > s.closing_time
            AND ( :now >= s.opening_time OR :now < s.closing_time )
          )
""", nativeQuery = true)
  List<Store> findStoreByDateTimeNow(@Param("now") LocalTime now);

  // :name 키워드 들어가는 스토어 모두 검색
  @Query(value = "SELECT * " +
          "FROM store s " +
          "WHERE s.name like %:name%", nativeQuery = true)
  List<Store> findStoresByName(String name);

  // 주소 기준 스토어 조회 목록
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

