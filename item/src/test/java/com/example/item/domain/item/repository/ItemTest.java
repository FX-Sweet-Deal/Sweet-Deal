package com.example.item.domain.item.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.global.domain.PositiveIntegerCount;
import com.example.item.domain.item.repository.enums.ItemStatus;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class ItemTest {
  private final Item item =
   Item.builder()
        .id(1L)
        .name("코코볼")
        .expiredAt(LocalDateTime.now().plusDays(10))
        .price(2000L)
        .quantity(new PositiveIntegerCount(100))
        .build();


  @Test
  @DisplayName("상품_등록_테스트")
  public void givenCreateItem_whenRegister_thenEqualsTrue() throws Exception {
    // given
    // when
    item.register();

    // then
    assertEquals(ItemStatus.SALE, item.getStatus());
    assertNotEquals(LocalDateTime.now(), item.getRegisteredAt());


  }

  @Test
  @DisplayName("상품_등록_해제_테스트")
  public void givenCreateItem_whenUnregister_thenEqualsTrue() throws Exception {
    // given
    // when
    item.register();
    item.unregister();

    // then
    assertEquals(ItemStatus.DELETED, item.getStatus());
    assertNotEquals(LocalDateTime.now(), item.getUnregisteredAt());


  }
  
  @Test
  @DisplayName("상품_상태_변경_테스트")
  public void givenRegisteredItem_whenChangeStatusToRESERVED_thenEqualsTrue() throws Exception {
    // given
    item.register();
    // when
    item.changeStatus(ItemStatus.RESERVED);

    // then
    assertEquals(ItemStatus.RESERVED, item.getStatus());

  }

  @Test
  @DisplayName("상품_수량_증가_테스트")
  public void givenCreateItem_whenQuantityIncrease_thenEqualsTrueQuantity() throws Exception {
    // given
    // when
    log.info("현재 수량 = {}", item.quantity());
    item.quantityIncrease(10);

    // then
    log.info("추가한 현재 수량 = {}", item.quantity());
    assertEquals(110, item.quantity());

  }

  @Test
  @DisplayName("상품_수량_감소_테스트")
  public void givenCreateItem_whenQuantityDecrease_thenEqualsTrueQuantity() throws Exception {
    // given
    // when
    log.info("현재 수량 = {}", item.quantity());
    item.quantityDecrease(10);

    // then
    log.info("추가한 현재 수량 = {}", item.quantity());
    assertEquals(90, item.quantity());

  }
}
