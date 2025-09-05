package com.example.user.domain.jwt.business;


import com.example.global.anntation.Business;
import com.example.global.errorcode.TokenErrorCode;
import com.example.global.resolver.UserRole;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.common.exception.jwt.TokenSignatureException;
import com.example.user.domain.jwt.converter.TokenConverter;
import com.example.user.domain.jwt.model.TokenClaimsData;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenEntity;
import com.example.user.domain.jwt.model.TokenResponse;
import com.example.user.domain.jwt.model.TokenValidationRequest;
import com.example.user.domain.jwt.model.TokenValidationResponse;
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
        UserRole userRole = userEntity.getRole();

        // Access/Refresh 토큰 발급
        TokenDto accessToken = tokenService.issueAccessToken(userId, userRole);
        TokenDto refreshToken = tokenService.issueRefreshToken(userId, userRole);

        // Refresh 토큰을 엔티티로 변환
        TokenEntity tokenEntity = tokenConverter.toRefreshTokenEntity(
            userEntity.getId(), refreshToken.getToken());

        // Redis에 기존 Refresh 삭제 후 새 Refresh 저장
        tokenService.deleteRefreshToken(userId);

        tokenService.saveRefreshToken(tokenEntity);

        return tokenConverter.toResponse(accessToken, refreshToken);
    }

    public TokenClaimsData validationAccessToken(String accessToken) {
        var userId = tokenService.validationToken(accessToken);

        return userId;
    }

    public TokenDto reIssueAccessToken(String refreshToken) {
        if (refreshToken == null) {
            throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN);
        }

        // "Bearer "가 있을 경우 제거
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        return tokenService.reIssueAccessToken(refreshToken);
    }




}
