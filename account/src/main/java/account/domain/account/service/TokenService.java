package account.domain.account.service;


import account.domain.account.ifs.TokenHelperIfs;
import com.example.global.errorcode.ErrorCode;
import com.example.global.errorcode.TokenErrorCode;
import com.example.user.common.exception.jwt.TokenException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenHelperIfs tokenHelperIfs;

    public Long validationToken(String token) {

        Map<String, Object> userData = tokenHelperIfs.validationTokenWithThrow(token);

        Object userId = userData.get("userId");
        Objects.requireNonNull(userId, () -> {
            throw new TokenException(TokenErrorCode.TOKEN_EXCEPTION);
        });

        return Long.parseLong(userId.toString());


    }
}
