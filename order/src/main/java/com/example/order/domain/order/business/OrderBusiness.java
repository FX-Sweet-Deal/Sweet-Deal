package com.example.order.domain.order.business;

import com.example.global.anntation.Business;
import com.example.global.api.Api;
import com.example.order.domain.common.response.MessageConverter;
import com.example.order.domain.common.response.MessageResponse;
import com.example.order.domain.order.controller.model.request.CancelRequest;
import com.example.order.domain.order.controller.model.request.MessageUpdateRequest;
import com.example.order.domain.order.controller.model.request.OrderItemRequest;
import com.example.order.domain.order.controller.model.request.PaymentRequest;
import com.example.order.domain.order.converter.OrderConverter;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.controller.model.request.OrderItemRegisterRequest;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.OrderItemRepository;
import com.example.order.domain.order.repository.Orders;
import com.example.order.domain.order.repository.Payment;
import com.example.order.domain.order.repository.enums.OrderStatus;
import com.example.order.domain.order.repository.enums.PaymentMethod;
import com.example.order.domain.order.service.ItemFeignClient;
import com.example.order.domain.order.service.OrderItemService;
import com.example.order.domain.order.service.OrderService;
import com.example.order.domain.order.service.StoreFeignClient;
import com.example.order.domain.order.service.model.item.ItemInform;
import com.example.order.domain.order.service.model.item.ItemInternalRequest;
import com.example.order.domain.order.service.model.item.ItemInternalResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class OrderBusiness {

  private final OrderService orderService;
  private final OrderItemService orderItemService;
  private final ItemFeignClient itemFeignClient;
  private final StoreFeignClient storeFeignClient;
  private final OrderConverter orderConverter;
  private final MessageConverter messageConverter;
  private final OrderItemRepository orderItemRepository;

  public OrderRegisterResponse order(OrderItemRegisterRequest orderItemRegisterRequests,
      PaymentMethod paymentMethod, Long userId) { // 임시 userId
    Orders order = Orders.builder()
        .payment(Payment.builder().paymentMethod(paymentMethod).build())
        .userId(userId)
        .totalPrice(0L)
        .build();
    orderService.order(order);

    List<OrderItemRequest> orderItemRequests = orderItemRegisterRequests.
        getOrderItems().stream().toList();

    boolean isValid = createOrderItems(orderItemRequests, order);// order 객체에 orderItem 저장됨.
    if (!isValid) {
      throw new IllegalArgumentException("주문이 취소되었습니다.");
    }

    return orderConverter.toRegisterResponse(order);

  }

  private boolean createOrderItems(List<OrderItemRequest> orderItemRequests, Orders order) {
    List<Long> itemIds = orderItemRequests.stream()
        .map(OrderItemRequest::getItemId)
        .toList();

    ItemInternalRequest itemInternalRequest = new ItemInternalRequest(itemIds);
    Api<ItemInternalResponse> response = itemFeignClient.getItem(itemInternalRequest);
    ItemInternalResponse itemInternalResponse = response.result();

    if (itemInternalResponse == null) {
      return false;
    }

    List<ItemInform> itemInforms = itemInternalResponse.getItemInforms();
    for (ItemInform itemInform : itemInforms) {

      for (OrderItemRequest orderItemRequest : orderItemRequests) {
        if (itemInform.getQuantity() < orderItemRequest.getQuantity()) {
          return false;
        }
        if (itemInform.getExpiredAt().isBefore(LocalDateTime.now())) {
          return false;
        }

        OrderItem orderItem = OrderItem.builder()
            .quantity(itemInform.getQuantity())
            .price(itemInform.getPrice())
            .totalPrice(itemInform.getPrice() * itemInform.getQuantity())
            .orderId(order)
            .itemId(itemInform.getId())
            .build();

        orderItemRepository.save(orderItem);

      }
    }

    return true;
  }


  public MessageResponse completePayment(PaymentRequest paymentRequest, Long userId) { // 임시 userId
    Orders order = orderService.getOrderByOrderIdAndStatus(paymentRequest.getOrderId(),
        OrderStatus.PENDING_PAYMENT); // 결제 대기중인 주문 조회

    // 결제 성공(가정)
    orderService.completePayment(order);

    // 유저 유효성 검사


    List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
    List<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).toList();
    ItemInternalRequest itemInternalRequest = new ItemInternalRequest(itemIds);
    Api<ItemInternalResponse> response = itemFeignClient.getItem(itemInternalRequest);
    ItemInternalResponse itemInternalResponse = response.result();

    if (itemInternalResponse == null) {
      throw new IllegalArgumentException("ITEM NOT FOUND");
    }

    Map<Long, ItemInform> itemInformMap = itemInternalResponse.getItemInforms().stream()
        .collect(Collectors.toMap(ItemInform::getId,
            Function.identity()));

    for (OrderItem orderItem : orderItems) {
      ItemInform itemInform = itemInformMap.get(orderItem.getId());

      if(itemInform.getQuantity() < orderItem.getQuantity()) {
        throw new IllegalArgumentException("");

      }

      if(itemInform.getExpiredAt().isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("유통기한이 지났습니다.");
      }
    }

    // orderItems의 재고 감소
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(order.getId(),orderItems
        .stream().map(orderConverter::toOrderItemRequests).toList());
    orderItemService.publishUpdateItem(messageUpdateRequest);

    // 주문 완료 상태
    orderService.completeOrder(order);

    return messageConverter.toResponse("결제가 완료되었습니다.");
  }

  public MessageResponse cancelOrder(CancelRequest cancelRequest, Long userId) {
    Orders order = orderService.getOrderByOrderId(cancelRequest.getId());

    // 유저 유효성 검사


    List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());

    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(order.getId(),
        orderItems.stream().map(orderConverter::toOrderItemRequests).toList());

    orderItemService.publishCancelItem(messageUpdateRequest);

    orderService.cancel(order);
    return messageConverter.toResponse("주문이 취소되었습니다.");
  }

  public MessageResponse cancelStoreOrder(CancelRequest cancelRequest, Long userId) {
    return null;
  }

}
