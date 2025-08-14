package com.example.item.domain.item.converter;

import com.example.global.anntation.Converter;
import com.example.item.domain.item.controller.model.response.ItemDetailResponse;
import com.example.item.domain.item.controller.model.response.ItemInform;
import com.example.item.domain.item.controller.model.response.ItemInternalResponse;
import com.example.item.domain.item.controller.model.response.ItemListResponse;
import com.example.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.response.ItemRegisterResponse;
import com.example.item.domain.item.repository.Item;
import java.util.List;

@Converter
public class ItemConverter {

  public Item toEntity(ItemRegisterRequest req, Long storeId) {
        return Item.builder()

            .name(req.getName())
            .quantity(req.getQuantity())
            .expiredAt(req.getExpiredAt())
            .price(req.getPrice())
            .storeId(storeId)
            .build();
  }

  public ItemRegisterResponse toResponse(Item item) {
        return ItemRegisterResponse.builder()
            .id(item.getId())
            .name(item.getName())
            .quantity((item.getQuantity()))
            .expiredAt(item.getExpiredAt())
            .registerAt(item.getRegisteredAt())
            .status(item.getStatus())
            .price(item.getPrice())
            .storeId(item.getStoreId())
            .build();

  }

  public List<ItemListResponse> toListResponse(List<Item> items) {
    return items.stream().map(item -> {
      return ItemListResponse.builder()
          .id(item.getId())
          .name(item.getName())
          .expiredAt(item.getExpiredAt())
          .quantity(item.getQuantity())
          .price(item.getPrice())
          .build();
    }).toList();
  }

  public ItemDetailResponse toDetailResponse(Item item) {
    return ItemDetailResponse.builder()
        .id(item.getId())
        .name(item.getName())
        .quantity(item.getQuantity())
        .expiredAt(item.getExpiredAt())
        .price(item.getPrice())
        .storeId(item.getStoreId())
        .build();
  }

  public ItemInternalResponse toInternalResponse(List<Item> items) {

    return ItemInternalResponse.builder()
        .itemInforms(
            items.stream().map(
                item ->{
                     return ItemInform.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                        .expiredAt(item.getExpiredAt())
                        .registerAt(item.getRegisteredAt())
                        .status(item.getStatus())
                        .price(item.getPrice())
                        .storeId(item.getStoreId())
                        .build();
                }
            ).toList()).build();
  }
}
