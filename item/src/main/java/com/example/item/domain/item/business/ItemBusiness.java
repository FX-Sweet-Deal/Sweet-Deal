package com.example.item.domain.item.business;

import com.example.global.anntation.Business;
import com.example.item.domain.item.controller.model.request.ItemDeleteRequest;
import com.example.item.domain.item.controller.model.response.StoreSimpleResponse;
import com.example.item.domain.item.converter.MessageConverter;
import com.example.item.domain.common.response.MessageResponse;
import com.example.item.domain.item.controller.model.request.ItemInternalRequest;
import com.example.item.domain.item.controller.model.response.ItemDetailResponse;
import com.example.item.domain.item.controller.model.response.ItemInternalResponse;
import com.example.item.domain.item.controller.model.response.ItemListResponse;
import com.example.item.domain.item.controller.model.request.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.response.ItemRegisterResponse;
import com.example.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.example.item.domain.item.converter.ItemConverter;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.enums.ItemStatus;
import com.example.item.domain.item.service.ItemService;
import com.example.item.domain.item.service.StoreFeignClient;
import feign.FeignException.FeignClientException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Business
@Slf4j
@RequiredArgsConstructor
public class ItemBusiness {

    private final ItemService itemService;
    private final ItemConverter itemConverter;
    private final MessageConverter messageConverter;
    private final StoreFeignClient storeFeignClient;

    // 상품 등록
    public ItemRegisterResponse register(ItemRegisterRequest req, Long fakeUserId) {

        try {
            Long userId = 0L;
            StoreSimpleResponse storeSimpleResponse = storeFeignClient.getStores(userId).result();
            List<Long> storesId = storeSimpleResponse.getStoresId();

            Long storeId = req.getStoreId();
            if (storeId == null) {
                if (storesId.size() == 1) {
                    storeId = storesId.get(0);
                } else {
                    throw new IllegalArgumentException("STORE_ID_REQUIRED");
                }
            }
            if (!storesId.contains(storeId)) {
                throw new IllegalArgumentException("STORE_NOT_OWNED");
            }

            Item registeredItem = itemConverter.toEntity(req, storeId);

            // 중복 상품 검증
            itemService.existsByItemWithThrow(storeId, req.getName(), req.getExpiredAt(),
                List.of(ItemStatus.SALE, ItemStatus.RESERVED));

            // 검증
            itemService.validateRegister(registeredItem);

            return itemConverter.toResponse(itemService.register(registeredItem));
        } catch (FeignClientException e) {
            throw new IllegalArgumentException("STORE_SERVICE_ERROR", e);

        }


    }

    // 상품 삭제
    public MessageResponse unregister(ItemDeleteRequest itemDeleteRequest, Long fakeUserId) {

        try {
            Long userId = 0L; // 삭제할 것
            StoreSimpleResponse storeSimpleResponse = storeFeignClient.getStores(userId).result();
            List<Long> storesId = storeSimpleResponse.getStoresId();
            log.info("{}", storesId.toString());

            // 상품이 존재하지 않을 시 예외 발생
            // 판매된 상품과 삭제된 상품은 제외 (SOLD, DELETED)
            itemService.notExistsByItemWithThrow(itemDeleteRequest.getItemId(), storesId);

            Item targetItem = itemService.getItemByIdAndStatusList(itemDeleteRequest.getItemId(),
                List.of(ItemStatus.SALE, ItemStatus.RESERVED));


            if(targetItem.getQuantity() != itemDeleteRequest.getQuantity()) {
                Long remainingQty = targetItem.remainingQuantity(itemDeleteRequest.getQuantity());
                Item item = itemConverter.deleteRemainingStockItem(targetItem, remainingQty);
                itemService.save(item);
            }

            itemService.unregister(targetItem, itemDeleteRequest.getQuantity());

            return messageConverter.toResponse("상품이 삭제되었습니다.");
        } catch (FeignClientException e) {
            throw new IllegalArgumentException("STORE_SERVICE_ERROR", e);
        }

    }

    /*
    판매된 상품은 30일 보관후 자동으로 삭제한다.
    (SOLD -> DELETED 변경)
    스케줄러에서 사용
     */
    public void deleteExpiredSoldItems() {

        LocalDateTime expiredDate = LocalDateTime.now().minusDays(30);

        itemService.deleteSoldItemAfter30Days(expiredDate);
    }

    /* 유통기한이 지났는지 확인 후 삭제 상태로 변경 스케줄러 사용*/
    public void deleteExpiredAtOver() {
        LocalDateTime present = LocalDateTime.now();

        itemService.expireItemsToDeleted(present);
    }

    /* 수정 */
    public MessageResponse update(Long itemId, ItemUpdateRequest request, Long fakeUserId) {

        try {
            Long userId = 0L;
            StoreSimpleResponse storeSimpleResponse = storeFeignClient.getStores(userId).result();
            List<Long> storesId = storeSimpleResponse.getStoresId();

            // 상품이 존재하지 않으면 예외 발생
            itemService.notExistsByItemWithThrow(itemId, storesId);

            Item targetItem = itemService.getItemByIdAndStatus(itemId, ItemStatus.SALE);

            itemService.update(request, targetItem);

            return messageConverter.toResponse("상품 정보가 수정되었습니다.");
        } catch (FeignClientException e) {
            throw new IllegalArgumentException("STORE_SERVICE_ERROR", e);
            }

    }

    public ItemDetailResponse getItemBy(Long itemId, Long fakeUserId) {

        try {
            Long userId = 0L; // 삭제할 것
            StoreSimpleResponse storeSimpleResponse = storeFeignClient.getStores(userId).result();
            List<Long> storesId = storeSimpleResponse.getStoresId();

            // 해당 스토어에 특정 상품이 없을 시 예외 발생
            itemService.notExistsByItemWithThrow(itemId, storesId);
            Item item = itemService.getItemById(itemId);

            return itemConverter.toDetailResponse(item);
        } catch (FeignClientException e) {
            throw new IllegalArgumentException("STORE_SERVICE_ERROR", e);

        }

    }

    public List<ItemListResponse> getItemListBy(Long fakeUserId) {
        try {
            Long userId = 0L; // 삭제할 것
            StoreSimpleResponse storeSimpleResponse = storeFeignClient.getStores(userId).result();
            List<Long> storesId = storeSimpleResponse.getStoresId();
            log.info("========{}========", storesId.toString());

            // storeId에 해당하는 SALE중인 상품 목록 조회
            List<Item> saleItemList = itemService.getItemListByStoresIdAndStatus(storesId,
                ItemStatus.SALE);

            return itemConverter.toListResponse(saleItemList);

        } catch (FeignClientException e) {
            throw new IllegalArgumentException("STORE_SERVICE_ERROR", e);
        }
    }

    public ItemInternalResponse getItemInternal(ItemInternalRequest itemInternalRequest) {
        List<Long> itemIds = itemInternalRequest.getItemIds().stream().toList();

        List<Item> items = new ArrayList<>();

        for (Long itemId : itemIds) {
            Item item = itemService.getItemById(itemId); // 상품의 정보들이 저장됨.
            items.add(item);
        }

        return itemConverter.toInternalResponse(items);
    }
}
