package com.example.order.domain.order.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "sweetDeal-store", path = "/internal")
public interface StoreFeignClient {



}
