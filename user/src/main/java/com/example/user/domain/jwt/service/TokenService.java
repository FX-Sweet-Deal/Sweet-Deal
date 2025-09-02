package com.example.user.domain.jwt.service;


import com.example.global.errorcode.TokenErrorCode;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.jwt.ifs.TokenHelperIfs;
import com.example.user.domain.jwt.model.TokenClaimsData;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenEntity;
import com.example.global.resolver.UserRole;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class  TokenService {

    private final TokenHelperIfs tokenHelperIfs;
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenDto issueAccessToken(Long userId, UserRole userRole) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("userRole", userRole);
        return tokenHelperIfs.issueAccessToken(data);
    }

    public TokenDto issueRefreshToken(Long userId, UserRole userRole) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("userRole", userRole);

        return tokenHelperIfs.issueRefreshToken(data);
    }

    public TokenDto reIssueAccessToken(String refreshToken) {
        TokenClaimsData tokenClaimsData = validationToken(refreshToken);

        String redisKey = "refresh:" + tokenClaimsData.getUserId();
        log.info("[토큰 재발급] Redis Key: {}", redisKey);
        log.info("[토큰 재발급] 받은 refreshToken: {}", refreshToken);

        Map<Object, Object> tokenData = redisTemplate.opsForHash().entries(redisKey);
        log.info("[토큰 재발급] Redis에서 조회한 데이터: {}", tokenData);

        String storedRefreshToken = (String) tokenData.get("refreshToken");
        log.info("[토큰 재발급] Redis에 저장된 refreshToken: {}", storedRefreshToken);

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            log.warn("[토큰 재발급 실패] 저장된 토큰과 요청 토큰이 일치하지 않음");
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }

        log.info("[토큰 재발급 성공] userId={}, role={}", tokenClaimsData.getUserId(), tokenClaimsData.getUserRole());
        return issueRefreshToken(tokenClaimsData.getUserId(), tokenClaimsData.getUserRole());
    }

    public TokenClaimsData validationToken(String token) {
        Map<String, Object> userData = tokenHelperIfs.validationTokenWithThrow(token);

        Object userId = userData.get("userId");
        Object userRole = userData.get("userRole");

        Objects.requireNonNull(userId, () -> {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        });
        return new TokenClaimsData(Long.parseLong(userId.toString()), UserRole.valueOf(userRole.toString()));
    }

    public void saveRefreshToken(TokenEntity tokenEntity) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("userId", tokenEntity.getUserId());
        tokenData.put("refreshToken", tokenEntity.getRefreshToken());

        String redisKey = "refresh:" + tokenEntity.getUserId(); // ✅ 키 통일
        redisTemplate.opsForHash().putAll(redisKey, tokenData);
        redisTemplate.expire(redisKey, 7, TimeUnit.DAYS); // 👈 TTL 설정도 추천
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }

}
