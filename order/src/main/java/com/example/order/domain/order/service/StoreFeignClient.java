package com.example.order.domain.order.service;

import com.example.global.api.Api;
import com.example.order.domain.order.controller.model.response.StoreSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store", path = "/internal")
public interface StoreFeignClient {

  @GetMapping(value = "/store/{userId}", headers = "X-Internal=true")
  Api<StoreSimpleResponse> getStore(@PathVariable Long userId);
}
