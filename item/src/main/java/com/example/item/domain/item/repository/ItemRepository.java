package com.example.item.domain.item.repository;

import com.example.item.domain.item.repository.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    /*
    기본 crud
     */

    /*
    중복 검증
    같은 스토어에 동일한 이름 유통기한 상품이 존재하는지 검증
     */
    Boolean existsByStoreIdAndNameAndExpiredAtAndStatus(Long storeId, String name, LocalDateTime expiredAt, ItemStatus status);

    /*
    상태별 & 유통기한 체크
    유통기한이 expiredAt 이후인 상품을 조회할 때
     */
    Optional<Item> findByIdAndStatusAndExpiredAtAfter(Long itemId, ItemStatus status, LocalDateTime expiredAtAfter);

    // 특정 id와 상태로 조회
    Optional<Item> findAllByIdAndStatus(Long itemId, ItemStatus status);


}
