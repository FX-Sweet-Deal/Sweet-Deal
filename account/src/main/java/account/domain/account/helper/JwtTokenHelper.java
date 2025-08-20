package account.domain.account.helper;

import account.domain.account.ifs.TokenHelperIfs;
import com.example.global.errorcode.TokenErrorCode;
import com.example.user.common.exception.jwt.TokenException;
import com.example.user.common.exception.jwt.TokenExpiredException;
import com.example.user.common.exception.jwt.TokenSignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenHelper implements TokenHelperIfs {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public Map<String, Object> validationTokenWithThrow(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());
        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

        try {
            Jws<Claims> result = parser.parseClaimsJws(token);
            return new HashMap<>(result.getBody());
        } catch (Exception e) {
            if (e instanceof SignatureException) { // 토큰이 유효하지 않을 때
                throw new TokenSignatureException(TokenErrorCode.INVALID_TOKEN);
            } else if (e instanceof ExpiredJwtException) { // 토큰 만료
                throw new TokenExpiredException(TokenErrorCode.EXPIRED_TOKEN);
            } else { // 그 외
                throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
            }
        }
    }

}
