package com.example.store.domain.common.config;

import com.example.global.interceptor.AuthorizationInterceptor;
import com.example.global.resolver.UserSessionResolver;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Bean
  public AuthorizationInterceptor authorizationInterceptor() {
    return new AuthorizationInterceptor();
  }

  private List<String> SWAGGER = List.of(
      "/swagger-ui.html",
      "/swagger-ui/**",
      "/v3/api-docs/**"
  );

  private List<String> DEFAULT_EXCLUDE = List.of(
      "/",
      "/favicon.ico",
      "/error"
  );

  private List<String> INTERNAL = List.of(
      "/internal/**"
  );


  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(authorizationInterceptor())
        .excludePathPatterns(SWAGGER) // 무시
        .excludePathPatterns(DEFAULT_EXCLUDE)
        .excludePathPatterns(INTERNAL);
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new UserSessionResolver());
  }
}
