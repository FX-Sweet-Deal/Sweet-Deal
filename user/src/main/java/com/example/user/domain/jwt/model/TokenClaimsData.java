package com.example.user.domain.jwt.model;

import com.example.user.domain.user.repository.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenClaimsData {

    Long userId;

    UserRole userRole;

}
