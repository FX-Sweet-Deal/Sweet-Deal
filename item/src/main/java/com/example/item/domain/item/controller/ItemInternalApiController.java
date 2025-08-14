package com.example.item.domain.item.controller;

import com.example.global.api.Api;
import com.example.item.domain.item.business.ItemBusiness;
import com.example.item.domain.item.controller.model.request.ItemInternalRequest;
import com.example.item.domain.item.controller.model.response.ItemInternalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class ItemInternalApiController {

  private final ItemBusiness itemBusiness;

  @PostMapping("/item")
  public Api<ItemInternalResponse> getItem(@RequestBody ItemInternalRequest itemInternalRequest) {
    ItemInternalResponse itemInternalResponse = itemBusiness.getItemInternal(itemInternalRequest);
    return Api.ok(itemInternalResponse);
  }


}
