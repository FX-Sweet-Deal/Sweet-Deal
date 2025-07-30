package com.example.order.domain.order.service;

import com.example.global.api.Api;
import com.example.order.domain.order.service.model.item.ItemInternalRequest;
import com.example.order.domain.order.service.model.item.ItemInternalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sweetDeal-items", path = "/internal")
public interface ItemFeignClient {

  @PostMapping(value = "item", headers = "X-Internal=true")
  Api<ItemInternalResponse> getItem(@RequestBody ItemInternalRequest itemInternalRequest);

}
