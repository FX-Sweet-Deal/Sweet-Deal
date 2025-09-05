package account.domain.account.helper;

import account.domain.account.ifs.TokenHelperIfs;
import com.example.global.errorCode.TokenErrorCode;
import com.example.user.domain.common.exception.jwt.TokenException;
import com.example.user.domain.common.exception.jwt.TokenExpiredException;
import com.example.user.domain.common.exception.jwt.TokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenHelper implements TokenHelperIfs {



    @Value("${token.secret.key}")
    private String secretKey;


    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {
        try {
            // 토근 null 체크 -> null이면 즉시 예외
            if (token == null){
                throw new IllegalArgumentException("token is null");
            }

            // Bearer 접두사 제거: Authorization 헤더에서 보통 "Bearer <token>" 형태로 들어오기 때문에 정규식으로 제거
            token = token.trim().replaceFirst("(?i)^Bearer\\s+","");

            // 따옴표 제거: JSON 직렬화 과정에서 토큰이 "eyJ..." 같은 문자열로 싸여 올 수 있음 -> 앞뒤 따옴표 제거
            if ((token.startsWith("\"") && token.endsWith("\"")) || (token.startsWith("'") && token.endsWith("'"))) {
                token = token.substring(1, token.length()-1);
            }

            // JWT는 header.payload.signature -> 반드시. 두 개 포함
            int len = token.length();
            long dots = token.chars().filter(c -> c=='.').count();
            log.info("JWT precheck: len={}, dots={}", len, dots);
            if (dots != 2) {
                log.warn("JWT format invalid (needs 3 segments): {}", token);
                throw new MalformedJwtException("JWT must have 3 segments");
            }

            // 2) 키 생성 (발급과 동일)
            SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

            // 3) 파싱 (JJWT 0.12)
            Jws<Claims> jws = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);

            return new HashMap<>(jws.getPayload());

        } catch (ExpiredJwtException e) {
            log.debug("JWT expired", e);
            throw new TokenExpiredException(TokenErrorCode.EXPIRED_TOKEN);

        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.debug("JWT invalid signature", e);
            throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN);

        } catch (DecodingException e) { // 🔴 Base64URL 디코딩 실패(토큰이 깨짐)
            log.debug("JWT decoding error", e);
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);

        } catch (MalformedJwtException | UnsupportedJwtException e) {
            log.debug("JWT malformed/unsupported", e);
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);

        } catch (WeakKeyException e) {
            log.debug("JWT weak key", e);
            throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN);

        } catch (JwtException e) { // 🔴 JJWT 공통 예외(위에서 못잡은 것들)
            log.debug("JWT general error", e);
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);

        } catch (IllegalArgumentException e) {
            log.debug("JWT illegal argument", e);
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);

        } catch (Exception e) {
            log.warn("JWT parse failed: {} - {}", e.getClass().getName(), e.getMessage(), e);
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        }
    }
}