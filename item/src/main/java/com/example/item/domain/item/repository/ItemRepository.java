package com.example.item.domain.item.repository;

import com.example.item.domain.item.repository.enums.ItemStatus;
import java.util.Collection;
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
    Boolean existsByStoreIdAndNameAndExpiredAtAndStatus(Long storeId, String name,
        LocalDateTime expiredAt, ItemStatus status);

    // SALE or RESERVED 중 유통기한 만료된 상품 조회
    List<Item> findAllByStatusInAndExpiredAtBefore(List<ItemStatus> status,
        LocalDateTime expiredAtBefore);

    List<Item> findAllByStatusAndUnregisteredAtBefore(ItemStatus status,
        LocalDateTime unregisteredAtBefore);

    // 특정 id와 상태로 조회
    Optional<Item> findByIdAndStatus(Long itemId, ItemStatus status);

    List<Item> findByStoreIdAndStatusOrderByIdDesc(Long storeId, ItemStatus status);

}

