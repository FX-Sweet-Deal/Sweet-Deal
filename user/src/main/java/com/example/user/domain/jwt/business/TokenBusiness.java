package com.example.user.domain.jwt.business;


import com.example.global.annotation.Business;
import com.example.global.errorcode.TokenErrorCode;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.jwt.converter.TokenConverter;
import com.example.user.domain.jwt.model.TokenClaimsData;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenEntity;
import com.example.user.domain.jwt.model.TokenResponse;
import com.example.user.domain.jwt.service.TokenService;
import com.example.user.domain.user.repository.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Business
public class TokenBusiness {

    private final TokenService tokenService;

    private final TokenConverter tokenConverter;
    @Transactional
    public TokenResponse issueToken(UserEntity userEntity) {

        if (userEntity == null) {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        }

        Long userId = userEntity.getId();

        TokenDto accessToken = tokenService.issueAccessToken(userId, userEntity.getRole());

        TokenDto refreshToken = tokenService.issueRefreshToken(userId, userEntity.getRole());

        TokenEntity tokenEntity = tokenConverter.toRefreshTokenEntity(
            userEntity.getId(), refreshToken.getToken());

        tokenService.deleteRefreshToken(userId);

        tokenService.saveRefreshToken(tokenEntity);

        return tokenConverter.toResponse(accessToken, refreshToken);
    }

    public TokenClaimsData validationAccessToken(String accessToken) {
        var userId = tokenService.validationToken(accessToken);

        return userId;
    }
}
