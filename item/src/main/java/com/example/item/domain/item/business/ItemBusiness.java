package com.example.item.domain.item.business;

import com.example.global.anntation.Business;
import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.ItemAlreadyExistsException;
import com.example.item.domain.common.exception.ItemCannotDeleteException;
import com.example.item.domain.item.controller.model.register.ItemRegisterRequest;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.ItemRepository;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class ItemBusiness {

    /*
    상품을 등록
     */
    public void register(Item item) {
        item.register();
    }

    /*
    상품을 등록할 수 있는지 검증
     */
    public void validateRegister(Item item) {
        if(item.getStatus() == ItemStatus.SALE) {
            throw new ItemAlreadyExistsException(ItemErrorCode.ITEM_ALREADY_EXISTS);
        }
    }

    /*
    상품을 삭제
     */
    public void unregister(Item item) {
        item.unregister();
    }

    /*
    삭제 가능한지 검증 (판매된 상품은 삭제 불가)
     */
    public void validateDeletable(Item item) {
        if (item.getStatus() == ItemStatus.SOLD) { // 상품이 판매된 상태일 때
            throw new ItemCannotDeleteException(ItemErrorCode.ITEM_DELETE_DENIED_SOLD);
        }
        if (item.getStatus() == ItemStatus.DELETED){ // 상품이 이미 삭제된 상태일 때
            throw new ItemCannotDeleteException(ItemErrorCode.ITEM_DELETE_DENIED_ALREADY);
        }

    }

    /*
    상품 판매
     */
    public void saleItem(Item item, Integer quantity) {

        item.quantityDecrease(quantity);

        item.changeStatus(ItemStatus.SOLD);
    }

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
    public void deleteSoldItemAfter30Days(List<Item> expiredItemList) {

        expiredItemList.forEach(item -> item.changeStatus(ItemStatus.DELETED));

    }


    /*
    삭제 상태로 변경
     */
    public void expireItems(List<Item> expiredOverItemList) {

        expiredOverItemList.forEach(item -> item.changeStatus(ItemStatus.DELETED));

    }

    /*
    중복 상품 검증
    item의 storeId, name, expiredAt, status 를 기준으로 검증
    같은 상품이 등록되어 있는 경우 예외 발생
     */
    public void existsByItemWithThrow(ItemRegisterRequest registerRequest, Long storeId) {


        if (exists) {
            throw new ItemAlreadyExistsException(ItemErrorCode.ITEM_ALREADY_EXISTS);
        }

    }

}
