package com.example.store.domain.store.service;

import com.example.global.errorcode.OpenApiErrorCode;
import com.example.store.domain.common.config.PublicDataProps;
import com.example.store.domain.common.exception.ntsBusiness.HttpException;
import com.example.store.domain.common.exception.ntsBusiness.OpenApiException;
import com.example.store.domain.store.controller.model.request.openApi.BusinessStatusRequest;
import com.example.store.domain.store.controller.model.request.openApi.BusinessValidateRequest;
import com.example.store.domain.store.controller.model.response.openApi.BusinessStatusResponse;
import com.example.store.domain.store.controller.model.response.openApi.BusinessValidateResponse;
import com.example.store.domain.store.repository.OpenApiErrorBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode;

@Data
@Slf4j
@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class OpenApiClient {

  private RestClient restClient;

  @Value("${publicData.api.service-key}")
  private String serviceKey;

  private ObjectMapper mapper;

  private OpenApiErrorBody OpenApiErrorBody;

  public OpenApiClient(RestClient.Builder builder, PublicDataProps props, ObjectMapper mapper) {
    Assert.hasText(props.getBaseUrl(), "publicData.api.base-url must not be empty");

    // EncodingMode.NONE을 설정하여 추가적인 인코딩을 하지 않음.
    DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(props.getBaseUrl());
    uriBuilderFactory.setEncodingMode(EncodingMode.NONE);

    this.restClient = builder
        .uriBuilderFactory(uriBuilderFactory)
        .build();

    this.serviceKey = Objects.requireNonNull(props.getServiceKey());
    this.mapper = mapper;
  }

  public BusinessValidateResponse validate(BusinessValidateRequest req) {
    return restClient.post()
        .uri(u -> u.path("/validate")
            .queryParam("serviceKey", serviceKey)
            .queryParam("returnType", "JSON")
            .build())
        // .header("Authorization", "Infuser " + serviceKey) // 헤더 인증(디코딩키) 사용시
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(req)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          byte[] bytes =
              response.getBody() != null ? response.getBody().readAllBytes() : new byte[0];
          log.info("",  response.getBody());

          OpenApiErrorBody body;
          try {
            body = mapper.readValue(bytes,
                com.example.store.domain.store.repository.OpenApiErrorBody.class);
          } catch (Exception e) {
            throw new HttpException(OpenApiErrorCode.HTTP_ERROR,
                new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
          }
          OpenApiErrorCode mapped = mapToEnum(body);
          String msg = body.getMsg() != null ? body.getMsg() : mapped.getDescription();
          throw new OpenApiException(mapped, msg);
        })
        .body(BusinessValidateResponse.class);
  }

  private OpenApiErrorCode mapToEnum(OpenApiErrorBody e) {
    // code 기반(-401, -4, -5)
    if (e.getCode() != null) {
      return switch (e.getCode()) {
        case -401 -> OpenApiErrorCode.MISSING_API_KEY;
        case -4 -> OpenApiErrorCode.INVALID_API_KEY;
        case -5 -> OpenApiErrorCode.HTTP_ERROR;
        default -> OpenApiErrorCode.HTTP_ERROR;
      };
    }
    // status_code 기반
    String sc = e.getStatusCode();
    if ("BAD_JSON_REQUEST".equals(sc))
      return OpenApiErrorCode.HTTP_ERROR.BAD_JSON_REQUEST;
    if ("TOO_LARGE_REQUEST".equals(sc))
      return OpenApiErrorCode.TOO_LARGE_REQUEST;
    if ("REQUEST_DATA_MALFORMED".equals(sc))
      return OpenApiErrorCode.HTTP_ERROR.REQUEST_DATA_MALFORMED;
    if ("INTERNAL_ERROR".equals(sc))
      return OpenApiErrorCode.INTERNAL_ERROR;

    return OpenApiErrorCode.HTTP_ERROR.HTTP_ERROR;
  }

  // 상태 조회: 사업자 번호 배열
  public BusinessStatusResponse status(BusinessStatusRequest req) {
    return restClient.post()
        .uri(u -> u.path("/status")
            .queryParam("serviceKey", serviceKey)   // 헤더 인증으로 바꾸면 이 줄 제거 + header 추가
            .queryParam("returnType", "JSON")
            .build())
        // .header("Authorization", "Infuser " + serviceKey) // (헤더 방식: 디코딩키일 때)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .body(req)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          byte[] bytes = response.getBody() != null ? response.getBody().readAllBytes() : new byte[0];
          OpenApiErrorBody body;
          try {
            body = mapper.readValue(bytes, OpenApiErrorBody.class);
          } catch (Exception parseFail) {
            throw new OpenApiException(OpenApiErrorCode.HTTP_ERROR,
                new String(bytes, StandardCharsets.UTF_8));
          }
          OpenApiErrorCode mapped = mapToEnum(body);
          String msg = body.getMsg() != null ? body.getMsg() : mapped.getDescription();
          throw new OpenApiException(mapped, msg);
        })
        .body(BusinessStatusResponse.class);
  }
}
