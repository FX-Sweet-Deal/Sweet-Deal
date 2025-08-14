package com.example.store.domain.store.controller;

import com.example.global.api.Api;
import com.example.store.domain.store.controller.model.request.openApi.BusinessStatusRequest;
import com.example.store.domain.store.controller.model.request.openApi.BusinessValidateRequest;
import com.example.store.domain.store.controller.model.response.openApi.BusinessStatusResponse;
import com.example.store.domain.store.controller.model.response.openApi.BusinessValidateResponse;
import com.example.store.domain.store.service.OpenApiClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  private final OpenApiClient openApiClient;

  // 진위 확인
  @PostMapping("/validate") // 200
  public ResponseEntity<BusinessValidateResponse> validate(@Valid @RequestBody BusinessValidateRequest req) {
    BusinessValidateResponse res = openApiClient.validate(req);
    return ResponseEntity.ok(res);
  }

  // 상태 조회
  @PostMapping("/status") // 200
  public ResponseEntity<BusinessStatusResponse> status(@RequestBody BusinessStatusRequest req) {
    BusinessStatusResponse res = openApiClient.status(req);
    return ResponseEntity.ok(res);
  }

}
