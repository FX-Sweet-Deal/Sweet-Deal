package com.example.global.resolver;

import com.example.global.annotation.UserSession;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// 컨트롤러 메서드에서 @UserSession User user 같은 커스텀 파라미터를 자동 주입해줍니다

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 체크, 어노테이션 체크

        // 파라미터가 @UserSession 애노테이션을 달았는지
        boolean annotation = parameter.hasParameterAnnotation(UserSession.class);

        // 타입이 User 클래스인지
        boolean parameterType = parameter.getParameterType().equals(User.class);

        return (annotation && parameterType);

    }


    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {

        // webRequest.getAttribute("userId"/"email"/"role", RequestAttributes.SCOPE_REQUEST) 읽음
        Object userIdAttr = webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Object emailAttr = webRequest.getAttribute("email", RequestAttributes.SCOPE_REQUEST);
        Object roleAttr  = webRequest.getAttribute("role",  RequestAttributes.SCOPE_REQUEST);

        // 방어로깅
        log.info("resolved attrs userId={}, email={}, role={}", userIdAttr, emailAttr, roleAttr);

        Long id = (userIdAttr != null && !userIdAttr.toString().isBlank())
            ? Long.parseLong(userIdAttr.toString())
            : null;

        // User.builder()로 User 객체를 만들어 반환
        return User.builder()
            .id(id)
            .email(emailAttr != null ? emailAttr.toString() : null)
            .role(roleAttr != null ? UserRole.valueOf(roleAttr.toString()) : null)
            .build();
    }
}
