package com.example.user.domain.jwt.helper;


import com.example.global.errorcode.TokenErrorCode;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.common.exception.jwt.TokenExpiredException;
import com.example.user.domain.common.exception.jwt.TokenSignatureException;
import com.example.user.domain.jwt.ifs.TokenHelperIfs;
import com.example.user.domain.jwt.model.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenHelper implements TokenHelperIfs {

    // JWT 서명에 사용할 비밀키
    @Value("${token.secret.key}")
    private String secretKey;

    // AccessToken 만료 시간
    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

    // Refresh Token 만료 시간
    @Value("${token.refresh-token.plus-hour}")
    private Long refreshTokenPlusHour;


    @Override
    public TokenDto issueAccessToken(Map<String, Object> data) {

        return getTokenDto(data, accessTokenPlusHour);
    }

    @Override
    public TokenDto issueRefreshToken(Map<String, Object> data) {

        return getTokenDto(data, refreshTokenPlusHour);
    }

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {


        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));


        JwtParser parser = Jwts.parser().setSigningKey(key).build();

        try{

            Jws<Claims> result = parser.parseClaimsJws(token);
            return new HashMap<>(result.getBody());

        } catch (Exception e) {

            if (e instanceof SignatureException){

               throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN);

            } else if(e instanceof ExpiredJwtException) {

                throw new TokenExpiredException(TokenErrorCode.EXPIRED_TOKEN);

            } else {

                throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);

            }
        }

    }

    private TokenDto getTokenDto(Map<String, Object> data, Long tokenPlusHour){
        LocalDateTime expiredLocalDateTime = LocalDateTime.now().plusHours(tokenPlusHour);

        Date expiredAt = Date.from(
            expiredLocalDateTime.atZone(
                ZoneId.systemDefault()
            ).toInstant()
        );

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());


        String jwtToken = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)  // HS256 알고리즘으로 서명
            .setClaims(data)  // payload(데이터) 저장
            .setExpiration(expiredAt)  // 만료일 설정
            .compact();  // 최종 JWT 문자열 생성

        return TokenDto.builder()
            .token(jwtToken)  // JWT 문자열
            .expiredAt(expiredLocalDateTime)  // 만료 시각
            .build();
    }
}
