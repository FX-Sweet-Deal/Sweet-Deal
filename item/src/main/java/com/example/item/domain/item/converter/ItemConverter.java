package com.example.item.domain.item.converter;

import com.example.global.anntation.Converter;
import com.example.global.domain.PositiveIntegerCount;
import com.example.item.domain.item.controller.model.detail.ItemDetailResponse;
import com.example.item.domain.item.controller.model.detail.ItemListResponse;
import com.example.item.domain.item.controller.model.register.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.register.ItemRegisterResponse;
import com.example.item.domain.item.repository.Item;
import java.util.List;

@Converter
public class ItemConverter {

  public Item toEntity(ItemRegisterRequest req, Long storeId) {
        return Item.builder()
            .name(req.getName())
            .quantity(new PositiveIntegerCount(req.getQuantity()))
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
          .quantity(item.quantity())
          .price(item.getPrice())
          .build();
    }).toList();
  }

  public ItemDetailResponse toDetailResponse(Item item) {
    return ItemDetailResponse.builder()
        .id(item.getId())
        .name(item.getName())
        .quantity(item.quantity())
        .expiredAt(item.getExpiredAt())
        .price(item.getPrice())
        .storeId(item.getStoreId())
        .build();
  }
}
