package com.example.image.domain.image.repository.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageStatus {

    REGISTERED("노출"),     // 노출 중
    UNREGISTERED("비노출(숨김)"),   // 비노출 (숨김)
    DELETED("삭제") ,   // 삭제 처리됨 (소프트 삭제 시 함께 사용)
    ;

    private final String description;
}
