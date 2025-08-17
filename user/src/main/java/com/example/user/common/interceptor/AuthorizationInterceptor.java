package com.example.user.common.interceptor;

import com.example.global.errorcode.ErrorCode;
import com.example.global.errorcode.TokenErrorCode;
import com.example.user.common.exception.jwt.TokenException;
import com.example.user.domain.jwt.business.TokenBusiness;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


@Slf4j
@RequiredArgsConstructor
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final TokenBusiness tokenBusiness;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB ,chrome 의 경우 GET, POST OPTRIONS = pass
        if(HttpMethod.OPTIONS.matches(request.getMethod())){
            return true;
        }

        // js. html. png resource 를 요청하는 경우 = pass
        if(handler instanceof ResourceHttpRequestHandler){
            return true;
        }

        // TODD header 검증
        var accessToken = request.getHeader("authorization-token");

        if(accessToken == null) {
            throw new TokenException(TokenErrorCode.AUTHORIZATION_TOKEN_NOT_FOUND);
        }

        var userId = tokenBusiness.validationAccessToken(accessToken);

        if(userId != null) {
            var requestContext = Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes());
            requestContext.setAttribute("userId", userId, RequestAttributes.SCOPE_REQUEST);
            return true;
        }

        throw new TokenException(ErrorCode.BAD_REQUEST, "인증실패");

    }
}
