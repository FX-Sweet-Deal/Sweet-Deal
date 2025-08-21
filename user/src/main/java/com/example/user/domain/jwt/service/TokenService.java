package com.example.user.domain.jwt.service;


import com.example.global.errorcode.TokenErrorCode;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.jwt.ifs.TokenHelperIfs;
import com.example.user.domain.jwt.model.TokenClaimsData;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenEntity;
import com.example.user.domain.user.repository.enums.UserRole;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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


        Map<Object, Object> tokenData  = redisTemplate.opsForHash().entries(String.valueOf(tokenClaimsData));

        String storedRefreshToken = (String) tokenData.get("refreshToken");

        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new TokenException(TokenErrorCode.INVALID_TOKEN);
        }
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
        redisTemplate.opsForHash().putAll(String.valueOf(tokenEntity.getUserId()), tokenData);
    }

    public void deleteRefreshToken(Long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }

}
