package com.example.store.domain.store.controller;

import com.example.global.api.Api;
import com.example.store.domain.store.business.StoreBusiness;
import com.example.store.domain.store.controller.model.request.LocationRequest;
import com.example.store.domain.store.controller.model.request.StoreRegisterRequest;
import com.example.store.domain.store.controller.model.request.StoreUpdateRequest;
import com.example.store.domain.store.controller.model.response.MessageResponse;
import com.example.store.domain.store.controller.model.response.NearbyStoreResponse;
import com.example.store.domain.store.controller.model.response.OwnerStoresResponse;
import com.example.store.domain.store.controller.model.response.StoreNameKeywordResponse;
import com.example.store.domain.store.controller.model.response.StoreRegisterResponse;
import com.example.store.domain.store.controller.model.response.UserStoreResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreApiController {

    public final StoreBusiness storeBusiness;

    @PostMapping("/register") // 200
    public Api<StoreRegisterResponse> register(@RequestBody @Valid StoreRegisterRequest storeRegisterRequest,
        Long userId) { // userId 임시 객체
        StoreRegisterResponse response = storeBusiness.register(storeRegisterRequest);
        return Api.ok(response);
    }

    @PostMapping("/{storeId}/unregister")
    public Api<MessageResponse> unregister(@PathVariable Long storeId, Long userId) {
        MessageResponse messageResponse = storeBusiness.unregister(storeId, userId);
        return Api.ok(messageResponse);
    }

    @PostMapping("/{storeId}/update") // 200
    public Api<MessageResponse> update(@PathVariable Long storeId,
        @RequestBody @Valid StoreUpdateRequest storeUpdateRequest) {
        MessageResponse messageResponse = storeBusiness.update(storeId, storeUpdateRequest);
        return Api.ok(messageResponse);
    }

    @GetMapping("/owner")
    public Api<List<OwnerStoresResponse>> ownerStores(Long userId) {
        List<OwnerStoresResponse> ownerStores = storeBusiness.getOwnerStore(userId);
        return Api.ok(ownerStores);
    }

    @GetMapping("/{storeId}") // 200
    public Api<UserStoreResponse> userStore(@PathVariable Long storeId) {
        UserStoreResponse userStoreResponse = storeBusiness.userStore(storeId);
        return Api.ok(userStoreResponse);
    }

    @GetMapping("/nearby")
    public Api<List<NearbyStoreResponse>> nearby(@PathVariable @Valid LocationRequest locationRequest) {
        List<NearbyStoreResponse> storesByNearby = storeBusiness.getStoresByNearby(locationRequest);
        return Api.ok((storesByNearby));
    }

    @GetMapping("/search") // 200
    public Api<List<StoreNameKeywordResponse>> nameKeyword(@RequestParam String name) {
        List<StoreNameKeywordResponse> stores = storeBusiness.getStoresByNameKeyword(
            name);
        return Api.ok(stores);
    }

}
