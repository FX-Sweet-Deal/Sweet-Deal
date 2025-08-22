package com.example.global.interceptor;

import com.example.global.errorcode.UserErrorCode;

import com.example.global.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


@Slf4j
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Authorization Interceptor url : {}", request.getRequestURI());

        // WEB ,chrome 의 경우 GET, POST OPTRIONS = pass
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        // js. html. png resource 를 요청하는 경우 = pass
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // Gateway 에서 전달한 Header 추출
        String userId = request.getHeader("x-user-id");
        String email = request.getHeader("x-user-email");
        String role = request.getHeader("x-user-role");

        if (userId == null) {
            throw new UserNotFoundException(UserErrorCode.USER_NOT_FOUND,
                "x-user-id header가 존재하지 않습니다.");
        }


        request.setAttribute("userId", userId);
        request.setAttribute("email", email);
        request.setAttribute("role", role);
        return true;
    }
}
