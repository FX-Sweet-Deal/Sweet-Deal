package com.example.item.domain.item.service;

import com.example.global.api.Api;
import com.example.item.domain.item.controller.model.response.StoreSimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "store", path = "/internal")
public interface StoreFeignClient {

  @GetMapping(value = "/store/{userId}", headers = "X-Internal=true")
  Api<StoreSimpleResponse> getStores(@PathVariable Long userId);
}
