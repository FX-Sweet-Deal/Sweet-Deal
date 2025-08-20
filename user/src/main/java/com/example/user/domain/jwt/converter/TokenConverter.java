package com.example.user.domain.jwt.converter;


import com.example.global.anntation.Converter;
import com.example.global.errorcode.ErrorCode;
import com.example.global.errorcode.TokenErrorCode;
import com.example.user.common.exception.jwt.TokenException;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenEntity;
import com.example.user.domain.jwt.model.TokenResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Converter
public class TokenConverter {

    public TokenEntity toRefreshTokenEntity(Long userId, String refreshToken) {
        return TokenEntity.builder()
            .userId(userId)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenResponse toResponse(
        TokenDto accessToken,
        TokenDto refreshToken
    ) {

        Objects.requireNonNull(accessToken, () -> {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        });

        Objects.requireNonNull(refreshToken, () -> {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        });


        return TokenResponse.builder()
            .accessToken(accessToken.getToken())
            .accessTokenExpiresAt(accessToken.getExpiredAt())
            .refreshToken(refreshToken.getToken())
            .refreshTokenExpiredAt(refreshToken.getExpiredAt())
            .build();

    }
}
