package com.example.item.repository;

import com.example.item.domain.Item;
import com.example.item.domain.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByIdAndStatus(Long itemId, ItemStatus status);

    List<Item> findByStoreIdAndStatusOrderByIdDesc(Long storeId, ItemStatus status);

    List<Item> findAllByStatus(ItemStatus status);

}
