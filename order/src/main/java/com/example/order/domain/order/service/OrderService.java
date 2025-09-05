package com.example.order.domain.order.service;

import com.example.global.errorcode.OrderErrorCode;
import com.example.order.domain.common.exception.order.InvalidOrderException;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.OrderRepository;
import com.example.order.domain.order.repository.Orders;
import com.example.order.domain.order.repository.enums.OrderStatus;
import com.example.order.domain.order.repository.enums.PaymentMethod;
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

  public void initPaymentMethod(Orders orders, PaymentMethod paymentMethod) {
    orders.initPaymentMethod(paymentMethod);
    orderRepository.save(orders);
  }

  public void addOrderItem(Orders orders, OrderItem orderItem) {
    orders.addOrderItems(orderItem);
  }

  public void calculateTotalPrice(Orders order) {
    order.calculateTotalPrice();
  }

  // 주문 취소
  public void cancel(Orders orders) {
    if(orders.getStatus() == OrderStatus.CANCELLED) {
      throw new IllegalArgumentException("이미 취소가 된 상품입니다.");
    }

//    if(orders.getStatus() == OrderStatus.COMPLETED) {
//      throw new IllegalArgumentException("이미 완료된 주문은 취소할 수 없습니다.");
//    }

    orders.cancel();
    orderRepository.save(orders);
  }

  // 결제 완료
  public void completePayment(Orders orders, boolean isSuccess) {
    if(orders.getStatus() != OrderStatus.PENDING_PAYMENT) {
      throw new IllegalArgumentException("결제를 처리할 수 없는 상태의 주문입니다.");
    }

    orders.changeStatus(OrderStatus.PROCESSING); // 주문 처리 중

    if(isSuccess) {
      orders.getPayment().complete();
      orders.getPayment().setPaymentAt();
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

    orders.complete();

    orderRepository.save(orders);
  }

  // 조회
  @Transactional(readOnly = true)
  public Orders getOrderByOrderId(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(()
        -> new InvalidOrderException(OrderErrorCode.ORDER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public Orders getOrderByOrderIdAndStatus(Long orderId, OrderStatus status) {
    return orderRepository.findByIdAndStatus(orderId, status)
        .orElseThrow(() -> new InvalidOrderException(OrderErrorCode.ORDER_NOT_FOUND));
  }

  // user가 주문한 주문 목록 조회
  @Transactional(readOnly = true)
  public List<Orders> getOrderListByUserId(Long userId) {

    List<Orders>  ordersList = orderRepository.findByUserId(userId);
    if(ordersList.isEmpty()) {
      throw new InvalidOrderException(OrderErrorCode.ORDER_NOT_FOUND);
    }
    return ordersList;
  }

  // 해당 스토어의 주문 목록 조회
  @Transactional(readOnly = true)
  public List<Orders> getOrderListByStoresId(List<Long> storesId) {
    List<Orders> orders = orderRepository.findByStoreIdIn(storesId);
    if(orders.isEmpty()) {
        throw new InvalidOrderException(OrderErrorCode.ORDER_NOT_FOUND);
    }
    return orders;
  }

  public Orders getOrderByIdPessimisticLock(Long orderId) {
    return orderRepository.findByIdPessimisticLock(orderId)
        .orElseThrow(() -> new IllegalArgumentException("ORDER_NOT_FOUND"));
  }
}
