package com.example.order.domain.order.service;

import com.example.order.domain.order.repository.OrderRepository;
import com.example.order.domain.order.repository.Orders;
import com.example.order.domain.order.repository.enums.OrderStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class OrderService {

  private final OrderRepository orderRepository;

  // 유효성 검증, 상태 변환

  // 주문 생성
  public void order(Orders orders) {
    if(orders.getStatus() != null) {
      throw new IllegalArgumentException("이미 등록된 상품입니다.");
    }
    orders.create();
    orderRepository.save(orders);
  }

  // 주문 취소
  public void cancel(Orders orders) {
    if(orders.getStatus() == OrderStatus.CANCELLED) {
      throw new IllegalArgumentException("이미 취소가 된 상품입니다.");
    }

    if(orders.getStatus() == OrderStatus.COMPLETED) {
      throw new IllegalArgumentException("이미 완료된 주문은 취소할 수 없습니다.");
    }

    orders.cancel();
    orderRepository.save(orders);
  }

  // 결제 완료
  public void completePayment(Orders orders) {
    if(orders.getStatus() != OrderStatus.PENDING_PAYMENT) {
      throw new IllegalArgumentException("결제를 처리할 수 없는 상태의 주문입니다.");
    }

    orders.changeStatus(OrderStatus.PROCESSING); // 주문 처리 중

    if(orders.isPaymentSuccess()) {
      orders.getPayment().complete();
    } else {
      orders.getPayment().fail();
    }
    orderRepository.save(orders);
  }

  // 주문 완료
  public void completeOrder(Orders orders) {
    if (orders.getStatus() != OrderStatus.PROCESSING) {
      throw new IllegalArgumentException("처리 중인 주문만 완료할 수 있습니다.");
    }
    if (!orders.isPaymentSuccess()) {
      throw new IllegalArgumentException("결제가 완료되지 않은 주문은 완료할 수 없습니다.");
    }

    orders.changeStatus(OrderStatus.COMPLETED);

    orderRepository.save(orders);
  }

  // 조회
  @Transactional(readOnly = true)
  public Orders getOrderByOrderId(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(()
        -> new IllegalArgumentException("ORDER NOT FOUND"));
  }

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

}
