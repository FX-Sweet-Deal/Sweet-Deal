package com.example.order.domain.order.converter;

import com.example.global.anntation.Converter;
import com.example.order.domain.order.controller.model.request.OrderItemRequest;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.Orders;

@Converter
public class OrderConverter {

  public OrderRegisterResponse toRegisterResponse(Orders order) {
    return OrderRegisterResponse.builder()
        .id(order.getId())
        .orderAt(order.getOrderedAt())
        .orderStatus(order.getStatus())
        .totalPrice(order.calculateTotalPrice())
        .orderItems(order.getOrderItems())
        .itemCount(order.countItem())
        .totalItemQuantity(order.countTotalItemQuantity())
        .paymentId(order.getPayment().getPaymentId())
        .paymentStatus(order.getPayment().getPaymentStatus())
        .paymentMethod(order.getPaymentMethod())
        .paymentAt(order.getPayment().getPaymentAt())
        .totalPayPrice(order.calculateTotalPrice())
        .paymentSuccess(order.isPaymentSuccess())
        .build();
  }

  public OrderItemRequest toOrderItemRequests(OrderItem orderItem) {
    return OrderItemRequest.builder()
        .itemId(orderItem.getItemId())
        .quantity(orderItem.getQuantity())
        .build();
  }

}
