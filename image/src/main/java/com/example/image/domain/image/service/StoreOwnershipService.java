package com.example.image.domain.image.service;

import com.example.image.domain.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreOwnershipService implements StoreOwnershipPort{


    private final ImageRepository imageRepository;

    @Override
    public boolean isManagerOrStore(Long userId, Long storeId) {
        return imageRepository.existsByIdAndUserId(storeId, userId);
    }

}
