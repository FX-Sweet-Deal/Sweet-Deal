package com.example.item.repository;

import com.example.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item add(Long id, Item storeId);

    /** 단일 조회 **/
    Optional<Item> findById(Long id);
    Optional<Item> findByName(String name);

    List<Item> findAll();





}
