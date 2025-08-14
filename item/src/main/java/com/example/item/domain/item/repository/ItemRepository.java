package com.example.item.domain.item.repository;

import com.example.item.domain.item.repository.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // SALE or RESERVED 중 존재하는 상품
    Boolean existsByStoreIdAndNameAndExpiredAtAndStatusIn(Long storeId, String name, LocalDateTime expiredAt, List<ItemStatus> statuses);

    // SALE or RESERVED 중 유통기한 만료된 상품 조회
    List<Item> findAllByStatusInAndExpiredAtBefore(List<ItemStatus> statuses,
        LocalDateTime expiredAtBefore);

    List<Item> findAllByStatusAndUnregisteredAtBefore(ItemStatus status,
        LocalDateTime unregisteredAtBefore);

    // 특정 id와 상태로 조회
    Optional<Item> findByIdAndStatus(Long itemId, ItemStatus status);

    List<Item> findByStoreIdAndStatusOrderByIdDesc(Long storeId, ItemStatus status);

    Boolean existsByIdAndStoreIdAndStatusIn(Long itemId, Long storeId, List<ItemStatus> statuses);

    Optional<Item> findByIdAndStatusIn(Long itemId, List<ItemStatus> statuses);

    Optional<Item> findFirstByIdAndStatusNotOrderByIdDesc(Long id, ItemStatus status);
}

