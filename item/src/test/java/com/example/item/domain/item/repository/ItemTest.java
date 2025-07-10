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

  @Test
  @DisplayName("상품_수량_업데이트_테스트")
  void givenCreateItem_whenUpdateQuantity_thenEqualsTrueQuantity() throws Exception {
    //given
    //when
    log.info("현재 수량 = {}", item.quantity());

    item.updateQuantity(10);

    log.info("업데이트한 수량 = {}", item.quantity());

    //then
    assertEquals(10, item.quantity());
  }

  @Test
  @DisplayName("상품_수량_예외_발생")
  void givenCreateItem_whenUpdateQuantity_thenExceptionThrow() throws Exception {
    //given
    //when
    //then
    String message = assertThrows(IllegalArgumentException.class,
        () -> item.updateQuantity(0)).getMessage();

    log.info(message);
  }

  @Test
  @DisplayName("상품_이름_업데이트_테스트")
  void givenCreateItem_whenRename_thenEqualsTrueName() throws Exception {
    //given
    //when
    log.info("현재 상품 이름 = {}", item.getName());

    item.rename("초코볼");

    log.info("업데이트한 상품 이름 = {}", item.getName());

    //then
    assertEquals("초코볼", item.getName());
  }

  @Test
  @DisplayName("상품_이름_예외_발생")
  void givenCreateItem_whenUpdateRename_thenExceptionThrow() throws Exception {
    //given
    //when
    //then
    String message = assertThrows(IllegalArgumentException.class,
        () -> item.rename("")).getMessage();

    log.info(message);
  }

  @Test
  @DisplayName("상품_유통기한_업데이트_테스트")
  void givenCreateItem_whenUpdateExpiredAt_thenEqualsFalseExpiredAt() throws Exception {
    //given
    //when
    log.info("현재 상품 유통기한 = {}", item.getExpiredAt());

    item.updateExpiredAt(LocalDateTime.now().plusDays(20));

    log.info("업데이트한 상품 유통기한 = {}", item.getExpiredAt());
    //then
  }

  @Test
  @DisplayName("상품_유통기한_예외_발생")
  void givenCreateItem_whenUpdateExpiredAt_thenExceptionThrow() throws Exception {
    //given
    //when
    //then
    String message = assertThrows(IllegalArgumentException.class,
        () -> item.updateExpiredAt(LocalDateTime.now().minusDays(50))).getMessage();

    log.info(message);
  }

  @Test
  @DisplayName("상품_가격_업데이트_테스트 ")
  public void givenCreateItem_whenUpdatePrice_thenEqualsTruePrice() throws Exception {
    // given
    // when
    log.info("현재 상품 가격 = {}", item.getPrice());

    item.updatePrice(1000L);

    log.info("업데이트한 상품 가격 = {}", item.getPrice());

    // then
    assertEquals(1000L, item.getPrice());

  }

  @Test
  @DisplayName("상품_가격_예외_발생")
  void givenCreateItem_whenUpdatePrice_thenExceptionThrow() throws Exception {
    //given
    //when
    //then
    String message = assertThrows(IllegalArgumentException.class,
        () -> item.updatePrice(-1000L)).getMessage();

    log.info(message);
  }
}
