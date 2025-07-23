package com.example.order.domain.order.business;

import com.example.global.anntation.Business;
import com.example.order.domain.order.repository.OrderRepository;
import com.example.order.domain.order.repository.Orders;
import com.example.order.domain.order.repository.enums.OrderStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class OrderBusiness {

  private final OrderRepository orderRepository;

  // 유효성 검증, 상태 변환

  // 주문 등록
  public void register(Orders orders) {
    orders.register();
    orderRepository.save(orders);
  }

  // 주문 취소
  public void cancel(Orders orders) {
    orders.cancel();
    orderRepository.save(orders);
  }

  // 조회
  @Transactional(readOnly = true)
  public Orders getOrderByOrderIdAndStatus(Long orderId, OrderStatus status) {
    return orderRepository.findByIdAndStatus(orderId, status)
        .orElseThrow(() -> new IllegalArgumentException("ORDER NOT FOUND"));
  }

  // user가 주문한 주문 목록 조회
  @Transactional(readOnly = true)
  public List<Orders> getOrderListByUserId(Long userId) {

    List<Orders>  ordersList = orderRepository.findByUserId(userId);
    if(ordersList.isEmpty()) {
      throw new IllegalArgumentException("ORDER NOT FOUND");
    }
    return ordersList;
  }

  // 해당 스토어의 주문 목록 조회
  @Transactional(readOnly = true)
  public List<Orders> getOrderListByStoreId(Long storeId) {
    List<Orders> ordersList = orderRepository.findByStoreId(storeId);
    if(ordersList.isEmpty()) {
        throw new IllegalArgumentException("ORDER NOT FOUND");
    }
    return ordersList;
  }

  // 수정 ?



}
