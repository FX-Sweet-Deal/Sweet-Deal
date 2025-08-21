package com.example.user.domain.common.resolver;

import com.example.global.anntation.UserSession;
import com.example.user.domain.user.repository.enums.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSessionResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 지원하는 파라미터 체크, 어노테이션 체크

        // 1. Annotation 있는지 확인
        boolean annotation = parameter.hasParameterAnnotation(UserSession.class);

        // 2. 파라미터의 타입 체크
        boolean parameterType = parameter.getParameterType().equals(User.class);

        return (annotation && parameterType);

    }


    @Override
    public Object resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) throws Exception {

        Object userIdAttr = webRequest.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Object emailAttr = webRequest.getAttribute("email", RequestAttributes.SCOPE_REQUEST);
        Object roleAttr  = webRequest.getAttribute("role",  RequestAttributes.SCOPE_REQUEST);

        // 방어로깅
        log.info("resolved attrs userId={}, email={}, role={}", userIdAttr, emailAttr, roleAttr);

        Long id = (userIdAttr != null && !userIdAttr.toString().isBlank())
            ? Long.parseLong(userIdAttr.toString())
            : null;

        return User.builder()
            .id(id)
            .email(emailAttr != null ? emailAttr.toString() : null)
            .role(roleAttr != null ? UserRole.valueOf(roleAttr.toString()) : null)
            .build();
    }
}
