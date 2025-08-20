package com.example.gateway.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {


    ADMIN("관리자"),
    USER("일반 사용자"),
    STORE("스토어 관리자")
    ;

    private final String description;
}
