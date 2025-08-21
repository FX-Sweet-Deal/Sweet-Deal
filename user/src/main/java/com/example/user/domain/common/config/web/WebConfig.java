package com.example.user.domain.common.config.web;

import com.example.user.domain.common.interceptor.AuthorizationInterceptor;
import com.example.user.domain.common.resolver.UserSessionResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    private List<String> OPEN_API = List.of(
        "/open-api/**"
    );

    private List<String> DEFAULT_EXCLUDE = List.of(
        "/",
        "/favicon.ico",
        "/error"
    );

    private List<String> SWAGGER = List.of(
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    );


    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
            .excludePathPatterns(DEFAULT_EXCLUDE)
            .excludePathPatterns(OPEN_API)
            .excludePathPatterns(SWAGGER);

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionResolver());
    }






}
