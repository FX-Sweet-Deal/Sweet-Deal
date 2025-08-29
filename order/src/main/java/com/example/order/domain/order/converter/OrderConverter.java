package com.example.order.domain.order.converter;

import com.example.global.anntation.Converter;
import com.example.order.domain.order.controller.model.request.OrderItemRequest;
import com.example.order.domain.order.controller.model.response.OrderItemResponse;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.controller.model.response.OrderResponse;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.Orders;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class OrderConverter {

  public OrderRegisterResponse toRegisterResponse(Orders order) {
    return OrderRegisterResponse.builder()
        .id(order.getId())
        .orderAt(order.getOrderedAt())
        .orderStatus(order.getStatus())
        .totalPrice(order.getTotalPrice())
        .orderItems(order.getOrderItems())
        .itemCount(order.countItem())
        .totalItemQuantity(order.countTotalItemQuantity())
        .paymentId(order.getPayment().getPaymentId())
        .paymentStatus(order.getPayment().getPaymentStatus())
        .paymentMethod(order.getPaymentMethod())
        .paymentAt(null)
        .totalPayPrice(order.getTotalPrice())
        .paymentSuccess(order.isPaymentSuccess())
        .build();
  }

  public OrderItemRequest toOrderItemRequests(OrderItem orderItem) {
    return OrderItemRequest.builder()
        .itemId(orderItem.getItemId())
        .quantity(orderItem.getQuantity())
        .build();
  }

  public List<OrderResponse> toOrderResponse(List<Orders> ordersList) {
    return ordersList.stream().map(orders ->
        OrderResponse.builder()
            .id(orders.getId())
            .orderedAt(orders.getOrderedAt())
            .cancelledAt(orders.getCancelledAt())
            .status(orders.getStatus())
            .totalPrice(orders.getTotalPrice())
            .userId(orders.getUserId())
            .storeId(orders.getStoreId())
            .build())
        .collect(Collectors.toList());
  }

  public List<OrderItemResponse> toOrderItemResponse(List<OrderItem> orderItems) {
    return orderItems.stream().map(orderItem ->
        OrderItemResponse.builder()
            .id(orderItem.getId())
            .quantity(orderItem.getQuantity())
            .price(orderItem.getOrderId())
            .totalPrice(orderItem.getTotalPrice())
            .itemId(orderItem.getItemId()).build()).toList();

  }

}
