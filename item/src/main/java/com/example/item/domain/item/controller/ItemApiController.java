package com.example.item.domain.item.controller;


import com.example.global.anntation.UserSession;
import com.example.global.api.Api;
import com.example.item.domain.item.controller.model.response.MessageResponse;
import com.example.item.domain.item.controller.model.request.ItemDeleteRequest;
import com.example.item.domain.item.controller.model.response.ItemDetailResponse;
import com.example.item.domain.item.controller.model.response.ItemListResponse;
import com.example.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.example.item.domain.item.business.ItemBusiness;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/item")
public class ItemApiController {

    private final ItemBusiness itemBusiness;

    @PostMapping("/register") // 200
    public Api<MessageResponse> register(
        @RequestBody @Valid ItemRegisterRequest request,
        @UserSession Long userId) { // userId 임시 객체

        MessageResponse response = itemBusiness.register(request, userId);

        return Api.ok(response);

    }

    @PostMapping("/unregister") // 200
    public Api<MessageResponse> unregister(
        @RequestBody ItemDeleteRequest itemDeleteRequest,
        @UserSession Long userId) {

        MessageResponse messageResponse = itemBusiness.unregister(itemDeleteRequest, userId);
        return Api.ok(messageResponse);
    }

    @PostMapping("/{itemId}/update") // 200
    public Api<MessageResponse> update(
        @PathVariable Long itemId,
        @RequestBody ItemUpdateRequest itemUpdateRequest,
        @UserSession Long userId) {

        MessageResponse messageResponse = itemBusiness.update(itemId, itemUpdateRequest, userId);
        return Api.ok(messageResponse);
    }


    @GetMapping("/{itemId}") // 200
    public Api<ItemDetailResponse> getItem(
        @PathVariable Long itemId,
        @UserSession Long userId) {

        ItemDetailResponse itemDetailResponse = itemBusiness.getItemBy(itemId, userId);
        return Api.ok(itemDetailResponse);
    }

    @GetMapping("/list") // 200
    public Api<List<ItemListResponse>> getItemList(@UserSession Long userId) {

        List<ItemListResponse> itemList = itemBusiness.getItemListBy(userId);
        return Api.ok(itemList);
    }
}