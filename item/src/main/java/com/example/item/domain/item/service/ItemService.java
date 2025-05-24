package com.example.item.domain.item.service;

import com.example.global.errorcode.ItemErrorCode;
import com.example.item.domain.common.exception.ItemNotFoundException;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.ItemRepository;
import com.example.item.domain.item.repository.ItemStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void register(Item item) {
        item.setStatus(ItemStatus.SALE);
        item.setRegistered_at(LocalDateTime.now());
        itemRepository.save(item);

    }

    public void unregister(Item item) {
        item.setStatus(ItemStatus.DELETED);
        item.setUnregistered_at(LocalDateTime.now());
        itemRepository.delete(item);

    }

    public Item getItemBy(Long itemId, ItemStatus status) {
        return itemRepository.findByIdAndStatus(itemId, status).
                orElseThrow(() -> new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND));
    }

    public List<Item> getItemListBy(Long storeId, ItemStatus status) {
        List<Item> itemList = itemRepository.findByStoreIdAndStatusOrderByIdDesc(storeId, status);
        if(itemList.isEmpty()) {
            throw new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND);
        }
        return itemList;
    }

    public List<Item> getAllItemListBy(ItemStatus status) {
        List<Item> allItemList = itemRepository.findAllByStatus(status);
        if(allItemList.isEmpty()) {
            throw new ItemNotFoundException(ItemErrorCode.ITEM_NOT_FOUND);
        }
        return allItemList;
    }

    /*
    public void update(ItemRegisterRequest item);
     */


}
