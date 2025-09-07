package com.example.order.domain.order.controller;

import com.example.global.anntation.UserSession;
import com.example.global.api.Api;
import com.example.global.resolver.User;
import com.example.order.domain.common.response.MessageResponse;
import com.example.order.domain.order.business.OrderBusiness;
import com.example.order.domain.order.controller.model.request.OrderRegisterRequest;
import com.example.order.domain.order.controller.model.request.PaymentRequest;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.controller.model.response.OrderResponse;
import io.swagger.v3.oas.annotations.Parameter;
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

  @PostMapping
  public Api<OrderRegisterResponse> create(
      @RequestBody OrderRegisterRequest orderRegisterRequest,
      @Parameter(hidden = true) @UserSession User user) {
    OrderRegisterResponse response = orderBusiness.order(orderRegisterRequest,
        user.getId());

    return Api.ok(response);
  }

  @PostMapping("/{orderId}/payment")
  public Api<MessageResponse> payment(
      @PathVariable("orderId") Long orderId,
      @RequestBody PaymentRequest paymentRequest,
      @Parameter(hidden = true) @UserSession User user) {

    MessageResponse messageResponse = orderBusiness.completePayment(orderId,
        paymentRequest, user.getId());
    return Api.ok(messageResponse);
  }

  // 고객이 주문 취소
  @PostMapping("{orderId}/cancel")
  public Api<MessageResponse> cancel(
      @PathVariable Long orderId,
      @Parameter(hidden = true) @UserSession User user) {
    MessageResponse messageResponse = orderBusiness.cancelOrder(orderId, user.getId());
    return Api.ok(messageResponse);
  }

  // 스토어 점주가 주문 취소
  @PostMapping("{orderId}/store/cancel")
  public Api<MessageResponse> cancelStore(
      @PathVariable Long orderId,
      @Parameter(hidden = true) @UserSession User user) {
    MessageResponse messageResponse = orderBusiness.cancelStoreOrder(orderId, user.getId());
    return Api.ok(messageResponse);
  }

  @GetMapping("/user") // 임시 userId 삭제
  public Api<List<OrderResponse>> getOrder(@Parameter(hidden = true) @UserSession User user) {
    List<OrderResponse> orderResponses = orderBusiness.getOrder(user.getId());
    return Api.ok(orderResponses);
  }

  @GetMapping("/store") // 임시 userId 삭제
  public Api<List<OrderResponse>> getStoreOrder(@Parameter(hidden = true) @UserSession User user) {
    List<OrderResponse> orderResponse = orderBusiness.getStoreOrder(user.getId());
    return Api.ok(orderResponse);
  }
}
