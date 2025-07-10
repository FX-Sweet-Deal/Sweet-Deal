package com.example.item.domain.item.service;

import com.example.item.domain.common.response.MessageConverter;
import com.example.item.domain.common.response.MessageResponse;
import com.example.item.domain.item.business.ItemBusiness;
import com.example.item.domain.item.controller.model.detail.ItemDetailResponse;
import com.example.item.domain.item.controller.model.detail.ItemListResponse;
import com.example.item.domain.item.controller.model.register.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.register.ItemRegisterResponse;
import com.example.item.domain.item.controller.model.update.ItemUpdateRequest;
import com.example.item.domain.item.converter.ItemConverter;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemBusiness itemBusiness;
    private final ItemConverter itemConverter;
    private final MessageConverter messageConverter;

    // 상품 등록
    public ItemRegisterResponse register(ItemRegisterRequest req, Long userId) {

        // kafka or feign 메시지 받기
        // storeRepository에서 userId와 storeId 검증(userId가 소유한 store 중에 storeId가 있는지)

        // storeService.findAllBy(userId);
        Long storeId = 0L; // 임시 작성 ( 변경할 것!)

        Item registeredItem = itemConverter.toEntity(req, storeId);

        // 중복 상품 검증
        itemBusiness.existsByItemWithThrow(storeId, req.getName(), req.getExpiredAt(),
            List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        // 검증
        itemBusiness.validateRegister(registeredItem);

        return itemConverter.toResponse(itemBusiness.register(registeredItem));

    }

    // 상품 삭제
    public MessageResponse unregister(Long itemId, Long userId) {

        // kafka 메시지 받기
        // storeId
        Long storeId = 0L;

        // 상품이 존재하지 않을 시 예외 발생
        // 판매된 상품과 삭제된 상품은 제외 (SOLD, DELETED)
        itemBusiness.notExistsByItemWithThrow(itemId, storeId);

        Item targetItem = itemBusiness.getItemByIdAndStatusList(itemId,
            List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        itemBusiness.unregister(targetItem);
        return messageConverter.toResponse("상품이 삭제되었습니다.");

    }

    /*
    판매된 상품은 30일 보관후 자동으로 삭제한다.
    (SOLD -> DELETED 변경)
    스케줄러에서 사용
     */
    public void deleteExpiredSoldItems() {

        LocalDateTime expiredDate = LocalDateTime.now().minusDays(30);

        itemBusiness.deleteSoldItemAfter30Days(expiredDate);
    }

    /* 유통기한이 지났는지 확인 후 삭제 상태로 변경 스케줄러 사용*/
    public void deleteExpiredAtOver() {
        LocalDateTime present = LocalDateTime.now();

        itemBusiness.expireItemsToDeleted(present);
    }

    /* 수정 */
    public MessageResponse update(ItemUpdateRequest request, Long userId) {
        // kafka
        Long storeId = 0L;

        // 상품이 존재하지 않으면 예외 발생
        itemBusiness.notExistsByItemWithThrow(request.getId(), storeId);

        Item targetItem = itemBusiness.getItemByIdAndStatus(request.getId(), ItemStatus.SALE);

        itemBusiness.update(request, targetItem);

        return messageConverter.toResponse("상품 정보가 수정되었습니다.");

    }

    public ItemDetailResponse getItemBy(Long itemId, Long userId) {
        // storeService.findAllByUserId(userId);
        // = storeid;
        Long storeId = 0L;

        // 해당 스토어에 특정 상품이 없을 시 예외 발생
        itemBusiness.notExistsByItemWithThrow(itemId, storeId);
        Item item = itemBusiness.getItemById(itemId);

        return itemConverter.toDetailResponse(item);

    }

    public List<ItemListResponse> getItemListBy(Long userId) {
        // storeService.findAllByUserId(userId);
        // = storeid;
        Long storeId = 0L;

        // storeId에 해당하는 SALE중인 상품 목록 조회
        List<Item> saleItemList = itemBusiness.getItemListByStoreIdAndStatus(storeId,
            ItemStatus.SALE);

       return itemConverter.toListResponse(saleItemList);
    }

}
