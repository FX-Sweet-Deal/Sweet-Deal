package com.example.store.domain.store.controller;

import com.example.global.api.Api;
import com.example.store.domain.store.business.StoreBusiness;
import com.example.store.domain.store.controller.model.response.StoreSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class StoreInternalApiController {

  private final StoreBusiness storeBusiness;

  @GetMapping("/store/{userId}")
  public Api<StoreSimpleResponse> getStore(@PathVariable Long userId) {
    StoreSimpleResponse storeSimpleResponse = storeBusiness.getSimpleStore(userId);
    return Api.ok(storeSimpleResponse);
  }

}
