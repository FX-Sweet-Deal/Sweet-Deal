package com.example.user.domain.jwt.helper;


import com.example.global.errorCode.TokenErrorCode;
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

    @Value("${token.secret.key}")
    private String secretKey;

    @Value("${token.access-token.plus-hour}")
    private Long accessTokenPlusHour;

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
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(data)
            .setExpiration(expiredAt)
            .compact();

        return TokenDto.builder()
            .token(jwtToken)
            .expiredAt(expiredLocalDateTime)
            .build();
    }
}
