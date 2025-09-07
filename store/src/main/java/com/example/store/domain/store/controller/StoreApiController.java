package com.example.store.domain.store.controller;

import com.example.global.anntation.UserSession;
import com.example.global.api.Api;
import com.example.global.resolver.User;
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
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/store")
public class StoreApiController {

    public final StoreBusiness storeBusiness;

    @PostMapping("/register") // 200
    public Api<StoreRegisterResponse> register(@RequestBody @Valid StoreRegisterRequest storeRegisterRequest,
        @Parameter(hidden = true) @UserSession User user) {
        log.info("====================register");

        StoreRegisterResponse response = storeBusiness.register(storeRegisterRequest, user.getId());
        return Api.ok(response);
    }

    @PostMapping("/{storeId}/unregister") // 200
    public Api<MessageResponse> unregister(@PathVariable Long storeId,
        @Parameter(hidden = true) @UserSession User user) {
        MessageResponse messageResponse = storeBusiness.unregister(storeId, user.getId());
        return Api.ok(messageResponse);
    }

    @PostMapping("/{storeId}/update") // 200
    public Api<MessageResponse> update(@PathVariable Long storeId,
        @RequestBody @Valid StoreUpdateRequest storeUpdateRequest) {
        MessageResponse messageResponse = storeBusiness.update(storeId, storeUpdateRequest);
        return Api.ok(messageResponse);
    }

    @GetMapping("/owner") // 200
    public Api<List<OwnerStoresResponse>> ownerStores(@Parameter(hidden = true) @UserSession User user) {
        List<OwnerStoresResponse> ownerStores = storeBusiness.getOwnerStore(user.getId());
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
