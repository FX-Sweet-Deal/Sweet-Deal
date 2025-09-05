package com.example.order.domain.order.controller;

import com.example.global.annotation.UserSession;
import com.example.global.api.Api;
import com.example.order.domain.common.response.MessageResponse;
import com.example.order.domain.order.business.OrderBusiness;
import com.example.order.domain.order.controller.model.request.OrderRegisterRequest;
import com.example.order.domain.order.controller.model.request.PaymentRequest;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.controller.model.response.OrderResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderApiController {

  private final OrderBusiness orderBusiness;

  @PostMapping // 200
  public Api<OrderRegisterResponse> create(
      @RequestBody OrderRegisterRequest orderRegisterRequest,
      @UserSession Long userId) { // 임시 userId
    OrderRegisterResponse response = orderBusiness.order(orderRegisterRequest,
        userId);

    return Api.ok(response);
  }

  @PostMapping("/{orderId}/payment") // 200
  public Api<MessageResponse> payment(
      @PathVariable("orderId") Long orderId,
      @RequestBody PaymentRequest paymentRequest,
      @UserSession Long userId) { // 임시 userId

    MessageResponse messageResponse = orderBusiness.completePayment(orderId,
        paymentRequest, userId);
    return Api.ok(messageResponse);
  }

  // 고객이 주문 취소
  @PostMapping("{orderId}/cancel")
  public Api<MessageResponse> cancel(
      @PathVariable Long orderId,
      @UserSession Long userId) {
    MessageResponse messageResponse = orderBusiness.cancelOrder(orderId, userId);
    return Api.ok(messageResponse);
  }

  // 스토어 점주가 주문 취소
  @PostMapping("{orderId}/store/cancel")
  public Api<MessageResponse> cancelStore(
      @PathVariable Long orderId,
      @UserSession Long userId) {
    MessageResponse messageResponse = orderBusiness.cancelStoreOrder(orderId, userId);
    return Api.ok(messageResponse);
  }

  @GetMapping("/user/{userId}") // 임시 userId 삭제
  public Api<List<OrderResponse>> getOrder(@PathVariable Long userId) {
    List<OrderResponse> orderResponses = orderBusiness.getOrder(userId);
    return Api.ok(orderResponses);
  }

  @GetMapping("/store/{userId}") // 임시 userId 삭제
  public Api<List<OrderResponse>> getStoreOrder(@PathVariable Long userId) {
    List<OrderResponse> orderResponse = orderBusiness.getStoreOrder(userId);
    return Api.ok(orderResponse);
  }
}
