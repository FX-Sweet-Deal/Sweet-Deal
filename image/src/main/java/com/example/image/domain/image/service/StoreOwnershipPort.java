package com.example.image.domain.image.service;

public interface StoreOwnershipPort {

    boolean isManagerOrStore(Long userId, Long storeId);
}
