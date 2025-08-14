package com.example.item.domain.item.controller;


import com.example.global.api.Api;
import com.example.item.domain.common.response.MessageResponse;
import com.example.item.domain.item.controller.model.response.ItemDetailResponse;
import com.example.item.domain.item.controller.model.response.ItemListResponse;
import com.example.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.response.ItemRegisterResponse;
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

    @PostMapping()
    public Api<ItemRegisterResponse> register(
        @RequestBody @Valid ItemRegisterRequest request,
        Long userId) { // userId 임시 객체

        ItemRegisterResponse response = itemBusiness.register(request, userId);

        return Api.ok(response);

    }

    @PostMapping("/unregister/{itemId}") // 수정
    public Api<MessageResponse> unregister(@PathVariable Long itemId, Long userId) {

        MessageResponse messageResponse = itemBusiness.unregister(itemId, userId);
        return Api.ok(messageResponse);
    }

    @PostMapping("/update") // 수정
    public Api<MessageResponse> update(@RequestBody ItemUpdateRequest itemUpdateRequest,
        Long userId) {

        MessageResponse messageResponse = itemBusiness.update(itemUpdateRequest, userId);
        return Api.ok(messageResponse);
    }


    @GetMapping("/{itemId}")
    public Api<ItemDetailResponse> getItem(@PathVariable Long itemId, Long userId) {

        ItemDetailResponse itemDetailResponse = itemBusiness.getItemBy(itemId, userId);
        return Api.ok(itemDetailResponse);
    }

    @GetMapping("/list")
    public Api<List<ItemListResponse>> getItemList(Long userId) {

        List<ItemListResponse> itemList = itemBusiness.getItemListBy(userId);
        return Api.ok(itemList);
    }

    /*
    스토어 매니저 조회용과
    고객 조회용 추가할 것..
     */


}