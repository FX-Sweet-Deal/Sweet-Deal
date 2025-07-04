package com.example.item.domain.item.service;

import com.example.global.domain.PositiveIntegerCount;
import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.ItemNotFoundException;
import com.example.item.domain.item.business.ItemBusiness;
import com.example.item.domain.item.controller.model.register.ItemRegisterRequest;
import com.example.item.domain.item.controller.model.register.ItemRegisterResponse;
import com.example.item.domain.item.controller.model.update.ItemUpdateRequest;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.ItemRepository;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemBusiness itemBusiness;

    // 상품 등록
    public ItemRegisterResponse register(ItemRegisterRequest registerRequest) {

        Item registeredItem =
            Item.builder()
                .name(registerRequest.getName())
                .quantity(new PositiveIntegerCount(registerRequest.getQuantity()))
                .expiredAt(registerRequest.getExpiredAt())
                .price(registerRequest.getPrice())
                .build();

        itemRepository.existsByStoreIdAndNameAndExpiredAtAndStatus(registerRequest, storeId);

        // 검증
        itemBusiness.validateRegister(registeredItem);
        itemBusiness.register(registeredItem);

      return new ItemRegisterResponse(itemRepository.save(registeredItem));

    }

    // 상품 삭제
    public void unregister(Long itemId) {
        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));

        itemBusiness.validateDeletable(item);
        itemBusiness.unregister(item);

        itemRepository.delete(item);
    }


    /*
    판매된 상품은 30일 보관후 자동으로 삭제한다.
    (SOLD -> DELETED 변경)
    스케줄러에서 사용
     */
    public void deleteExpiredSoldItems() {

        LocalDateTime expiredDate = LocalDateTime.now().minusDays(30);

        List<Item> expiredItemList = itemRepository.findAllByStatusAndUnregisteredAtBefore(
            ItemStatus.SOLD, expiredDate);

        itemBusiness.deleteSoldItemAfter30Days(expiredItemList);

        itemRepository.saveAll(expiredItemList);

    }

    /* 유통기한이 지났는지 확인 후 삭제 상태로 변경 스케줄러 사용*/
    public void deleteExpiredAtOver() {
        LocalDateTime present = LocalDateTime.now();

        // 상품 상태가 SALE or RESERVED
        // where status in ('SALE' , 'RESERVED')
        List<Item> expiredAtOverItemList = itemRepository.findAllByStatusInAndExpiredAtBefore(
            List.of(ItemStatus.SALE, ItemStatus.RESERVED),
            present);

        itemBusiness.expireItems(expiredAtOverItemList);

        itemRepository.saveAll(expiredAtOverItemList);

    }

    // id와 status로 아이템 조회
    @Transactional(readOnly = true)
    public Item getItemBy(Long itemId, ItemStatus status) {
        return itemRepository.findByIdAndStatus(itemId, status).
                orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));
    }

    // 해당 스토어에 상태에 따라 상품 조회
    @Transactional(readOnly = true)
    public List<Item> getItemListBy(Long storeId, ItemStatus status) {
        List<Item> itemList = itemRepository.findByStoreIdAndStatusOrderByIdDesc(storeId, status);
        if(itemList.isEmpty()) {
            throw new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND);
        }
        return itemList;
    }

    /* 수정 */
    public void update(ItemUpdateRequest itemUpdateRequest) {


    }



}
