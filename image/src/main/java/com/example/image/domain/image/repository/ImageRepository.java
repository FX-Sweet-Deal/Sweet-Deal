package com.example.image.domain.image.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    Optional<ImageEntity> findByIdAndDeletedFalse(Long id);

    List<ImageEntity> findByItemIdAndDeletedFalse(Long itemId);

    List<ImageEntity> findByStoreIdAndDeletedFalse(Long storeId);
}
