package com.example.store.domain.store.controller;

import com.example.global.api.Api;
import com.example.store.domain.store.controller.model.request.BusinessStatusRequest;
import com.example.store.domain.store.controller.model.request.BusinessValidateRequest;
import com.example.store.domain.store.service.OpenApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nts-businessman/v1")
@Validated
public class NtsController {

  private OpenApiClient openApiClient;


  // 진위 확인
  @PostMapping("/validate")
  public Api<String> validate(@RequestBody BusinessValidateRequest req) {
    String json = openApiClient.validate(req.getB_no(), req.getStart_dt(), req.getP_nm());
    return Api.ok(json);
  }

  // 상태 조회
  @PostMapping("/status")
  public Api<String> status(@RequestBody BusinessStatusRequest req) {
    String json = openApiClient.status(req.getBusinessNos());
    return Api.ok(json);
  }



}
