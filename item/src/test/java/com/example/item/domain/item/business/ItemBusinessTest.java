package com.example.item.domain.item.business;

import static org.junit.jupiter.api.Assertions.*;

import com.example.global.domain.PositiveIntegerCount;
import com.example.item.domain.item.repository.Item;
import com.example.item.domain.item.repository.ItemRepository;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemBusinessTest {

  @Autowired
  private ItemBusiness itemBusiness;
  @Autowired
  private  ItemRepository itemRepository;

  private Item item;

  @BeforeEach
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void cleanDB() {
    itemRepository.deleteAll();
  }

  @BeforeEach
  public void init() {
    item =
        Item.builder()
            .name("코코볼")
            .expiredAt(LocalDateTime.now().plusDays(10))
            .price(2000L)
            .quantity(new PositiveIntegerCount(100))
            .build();

  }

  @Test
  @DisplayName("상품_등록_테스트")
  public void givenCreateItem_whenRegister_thenSavedItem() throws Exception {
    // given
    // when
    Item registeredItem = itemBusiness.register(item);

    Item findItem = itemRepository.findById(registeredItem.getId())
        .orElseThrow(() -> new IllegalArgumentException());
    // then
    assertEquals(registeredItem.getId(), findItem.getId());

  }

  @Test
  @DisplayName("상품_상태_삭제_테스트")
  public void givenRegisteredItem_whenUnregister_thenItemStatusDELETED() throws Exception {
    // given
    itemBusiness.register(item);
    // when
    itemBusiness.unregister(item);

    Item savedItem = itemRepository.findById(item.getId())
        .orElseThrow(() -> new IllegalArgumentException());

    // then
    assertEquals(savedItem.getStatus(), ItemStatus.DELETED);
  }
}