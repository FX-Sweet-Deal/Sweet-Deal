package com.example.store.domain.common.config;

import com.example.store.domain.store.service.OpenApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PublicDataProps.class)
public class OpenApiConfig {

  private final PublicDataProps props;
  private final ObjectMapper mapper;

  @Bean
  OpenApiClient openApiClient(RestClient.Builder builder) {
    return new OpenApiClient(builder, props, mapper);
  }

}
