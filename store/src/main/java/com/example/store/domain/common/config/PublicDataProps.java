package com.example.store.domain.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "publicdata.api")
@Data
public class PublicDataProps {
  private String baseUrl;
  private String serviceKey;  // service-key 자동 매핑
}

