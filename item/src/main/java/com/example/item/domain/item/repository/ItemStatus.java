package com.example.item.domain.item.repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ItemStatus {
    SALE("판매중"),
    SOLD("판매됨"),
    RESERVED("예약중"),
    DELETED("삭제됨");

    private final String description;
}
