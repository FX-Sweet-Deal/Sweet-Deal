package com.example.gateway.filter;

import com.example.gateway.common.error.TokenErrorCode;
import com.example.gateway.common.exception.token.NotPermittedException;
import com.example.gateway.common.exception.token.TokenException;
import com.example.gateway.common.exception.token.TokenExpiredException;
import com.example.gateway.common.exception.token.TokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private static final String PUBLIC_API_PREFIX = "/open-api";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int AUTH_HEADER_BEGIN_INDEX = BEARER_PREFIX.length();

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains(PUBLIC_API_PREFIX)) {
            return chain.filter(exchange);
        }
        return filterPrivateApi(exchange, chain);
    }

    private Mono<Void> filterPrivateApi(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = getAuthHeader(exchange);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new NotPermittedException(TokenErrorCode.NOT_PERMITTED);
        }
        String accessToken = authHeader.substring(AUTH_HEADER_BEGIN_INDEX);
        return validateToken(exchange, chain, accessToken);
    }

    private String getAuthHeader(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        return headers.getFirst(HttpHeaders.AUTHORIZATION);
    }

    private Mono<Void> validateToken(ServerWebExchange exchange,
        GatewayFilterChain chain,
        String accessToken) {

        Map<String, Object> tokenInfo = validationTokenWithThrow(accessToken);
        String userId = String.valueOf(tokenInfo.get("userId"));
        String userRole = String.valueOf(tokenInfo.get("userRole"));

        ServerHttpRequest request = exchange.getRequest().mutate()
            .header("x-user-id", userId)
            .header("x-user-role", userRole)
            .build();

        return chain.filter(exchange.mutate().request(request).build());
    }


    private Map<String, Object> validationTokenWithThrow(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        try {
            // 0.12.x: verifyWith(key) + parseSignedClaims(token)
            Jws<Claims> result = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return new HashMap<>(result.getPayload()); // 0.12.x에서 getPayload()
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN, e);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new TokenExpiredException(TokenErrorCode.EXPIRED_TOKEN, e);
        } catch (Exception e) {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION, e);
        }
    }

    @Override
    public int getOrder() {
        return -1; // 인증을 먼저 수행
    }
}