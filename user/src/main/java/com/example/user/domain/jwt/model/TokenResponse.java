package com.example.user.domain.jwt.model;

import java.time.LocalDateTime;

public class TokenResponse {

    private String accessToken;

    private LocalDateTime accessTokenExpiresAt;

    private String refreshToken;

    private LocalDateTime refreshTokenExpiredAt;



}
