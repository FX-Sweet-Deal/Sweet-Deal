package com.example.image.domain.image.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    // 단건 (삭제되지 않은 것만)
    Optional<ImageEntity> findByIdAndDeletedFalse(Long id);

    // 권한 체크 겸 단건
    Optional<ImageEntity> findByIdAndStoreIdAndDeletedFalse(Long id, Long storeId);

    // 상품별
    List<ImageEntity> findAllByItemIdAndDeletedFalse(Long itemId);

    // 스토어별
    List<ImageEntity> findAllByStoreIdAndDeletedFalse(Long storeId);

    // 존재 여부(권한 확인에 유용)
    boolean existsByIdAndStoreIdAndDeletedFalse(Long id, Long storeId);
}
