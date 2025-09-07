package com.example.item.domain.item.controller;


import com.example.global.anntation.UserSession;
import com.example.global.api.Api;
import com.example.global.resolver.User;
import com.example.item.domain.item.controller.model.response.MessageResponse;
import com.example.item.domain.item.controller.model.request.ItemDeleteRequest;
import com.example.item.domain.item.controller.model.response.ItemDetailResponse;
import com.example.item.domain.item.controller.model.response.ItemListResponse;
import com.example.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.example.item.domain.item.business.ItemBusiness;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("/register")
    public Api<MessageResponse> register(
        @RequestBody @Valid ItemRegisterRequest request,
        @Parameter(hidden = true) @UserSession User user) {

        MessageResponse response = itemBusiness.register(request, user.getId());
        return Api.ok(response);

    }

    @PostMapping("/unregister")
    public Api<MessageResponse> unregister(
        @RequestBody ItemDeleteRequest itemDeleteRequest,
        @Parameter(hidden = true) @UserSession User user) {

        MessageResponse messageResponse = itemBusiness.unregister(itemDeleteRequest, user.getId());
        return Api.ok(messageResponse);
    }

    @PostMapping("/{itemId}/update")
    public Api<MessageResponse> update(
        @PathVariable Long itemId,
        @RequestBody ItemUpdateRequest itemUpdateRequest,
        @Parameter(hidden = true) @UserSession User user) {

        MessageResponse messageResponse = itemBusiness.update(itemId, itemUpdateRequest,
            user.getId());
        return Api.ok(messageResponse);
    }


    @GetMapping("/{itemId}")
    public Api<ItemDetailResponse> getItem(
        @PathVariable Long itemId,
        @Parameter(hidden = true) @UserSession User user) {

        ItemDetailResponse itemDetailResponse = itemBusiness.getItemBy(itemId, user.getId());
        return Api.ok(itemDetailResponse);
    }

    @GetMapping("/list")
    public Api<List<ItemListResponse>> getItemList(@Parameter(hidden = true) @UserSession User user) {

        List<ItemListResponse> itemList = itemBusiness.getItemListBy(user.getId());
        return Api.ok(itemList);
    }
}