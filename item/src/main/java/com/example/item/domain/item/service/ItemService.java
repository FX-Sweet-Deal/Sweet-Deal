package com.example.item.domain.item.service;

import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.item.ItemAlreadyExistsException;
import com.example.item.domain.common.exception.item.ItemCannotDeleteException;
import com.example.item.domain.common.exception.item.ItemNotFoundException;
import com.example.item.domain.item.controller.model.request.ItemUpdateRequest;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.ItemRepository;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class ItemService {

    private final ItemRepository itemRepository;

    /*
    상품을 등록
     */
    public Item register(Item item) {
        item.register();
        return itemRepository.save(item);
    }

    /*
    상품을 등록할 수 있는지 검증
     */
    public void validateRegister(Item item) {
        if(item.getStatus() == ItemStatus.SALE) {
            throw new ItemAlreadyExistsException(ItemErrorCode.ITEM_ALREADY_EXISTS); // 수정
        }
    }

    /*
    상품을 삭제
     */
    public void unregister(Item item) {
        item.unregister();
        itemRepository.save(item);
    }

    /*
    삭제 가능한지 검증 (판매된 상품은 삭제 불가)
     */
    public void validateDeletable(Item item) {
        if (item.getStatus() == ItemStatus.SOLD) { // 상품이 판매된 상태일 때
            throw new ItemCannotDeleteException(ItemErrorCode.ITEM_ALREADY_SOLD);
        }
        if (item.getStatus() == ItemStatus.DELETED){ // 상품이 이미 삭제된 상태일 때
            throw new ItemCannotDeleteException(ItemErrorCode.ITEM_ALREADY_DELETED);
        }

    }

    /*
    상품 판매
     */
//    public void saleItem(Item item, Long quantity) {
//
//        item.quantityDecrease(quantity);
//
//        item.changeStatus(ItemStatus.SOLD);
//    }

    /*
    상품이 판매 가능한지 검증
     */
    public void validateSaleable(Item item) {
        if(item.getStatus() != ItemStatus.SALE) { // 상품이 판매중인 상태가 아닐 때
            throw new IllegalArgumentException("판매중인 상품이 아닙니다.");
        }
    }

    /*
    판매된 상품은 30일 보관후 자동으로 삭제한다.
     */
    public void deleteSoldItemAfter30Days(LocalDateTime expiredDate) {

        List<Item> expiredItemList = itemRepository.findAllByStatusAndUnregisteredAtBefore(
            ItemStatus.SOLD, expiredDate);

        expiredItemList.forEach(item -> item.changeStatus(ItemStatus.DELETED));

        itemRepository.saveAll(expiredItemList);
    }


    /*
    현재 시간 기준으로 유통기간이 지난 상품 상태 DELETED로 자동 변경
    삭제 상태로 변경
     */
    public void expireItemsToDeleted(LocalDateTime present) {

        // 상품 상태가 SALE or RESERVED
        // where status in ('SALE', 'RESERVED')
        List<Item> expiredAtOverItemList = itemRepository.findAllByStatusInAndExpiredAtBefore(
            List.of(ItemStatus.SALE, ItemStatus.RESERVED), present);

        expiredAtOverItemList.forEach(item -> item.changeStatus(ItemStatus.DELETED));

        itemRepository.saveAll(expiredAtOverItemList);
    }

    /*
    중복 상품 검증
    같은 상품이 등록되어 있는 경우 예외 발생
     */
    public void existsByItemWithThrow(Long storeId, String name, LocalDateTime expiredAt,
        List<ItemStatus> itemStatuses) {

        // 상품이 존재하는지 검증
        Boolean existsByItem = itemRepository.existsByStoreIdAndNameAndExpiredAtAndStatusIn(storeId,
            name, expiredAt, List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        if(existsByItem) {
            throw new ItemAlreadyExistsException(ItemErrorCode.ITEM_ALREADY_EXISTS);
        }

    }

    // storeId에 itemId가 존재하지 않을 때
    public void notExistsByItemWithThrow(Long itemId, Long storeId) {

        Boolean existByItem = itemRepository.existsByIdAndStoreIdAndStatusIn(itemId, storeId,
            List.of(ItemStatus.SALE, ItemStatus.RESERVED));

        if(!existByItem) {
            throw new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND);
        }
    }

    // id와 status로 아이템 조회
    @Transactional(readOnly = true)
    public Item getItemByIdAndStatus(Long itemId, ItemStatus status) {
        return itemRepository.findByIdAndStatus(itemId, status).
            orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));
    }

    // id와 statusList로 아이템 조회
    @Transactional(readOnly = true)
    public Item getItemByIdAndStatusList(Long itemId, List<ItemStatus> statuses) {
        return itemRepository.findByIdAndStatusIn(itemId, statuses).
            orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId).
            orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));

    }

    // 해당 스토어에 상품 상태에 따라 상품 조회
    @Transactional(readOnly = true)
    public List<Item> getItemListByStoreIdAndStatus(Long storeId, ItemStatus status) {

        // 상품 리스트가 완전히 비어있을 때, 예외 발생
        List<Item> itemList = itemRepository.findByStoreIdAndStatusOrderByIdDesc(storeId, status);
        if(itemList.isEmpty()) {
            throw new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND);
        }
        return itemList;
    }

    public void update(ItemUpdateRequest request, Item targetItem) {
        targetItem.rename(request.getName());
        targetItem.updateStatus(request.getStatus());
        targetItem.updateExpiredAt(request.getExpiredAt());
        targetItem.updatePrice(request.getPrice());
        targetItem.updateQuantity(request.getQuantity());
        itemRepository.save(targetItem);
    }

    public Item getItemByIdAndStatusNot(Long itemId) {
        return itemRepository.findFirstByIdAndStatusNotOrderByIdDesc(itemId, ItemStatus.DELETED)
            .orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));
    }
}




