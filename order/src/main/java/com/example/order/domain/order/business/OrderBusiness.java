package com.example.order.domain.order.business;

import com.example.global.anntation.Business;
import com.example.global.api.Api;
import com.example.global.errorcode.OrderErrorCode;
import com.example.order.domain.common.exception.order.ExpiredException;
import com.example.order.domain.common.exception.order.ItemCountNotEnoughException;
import com.example.order.domain.common.exception.order.ItemNotExistFoundException;
import com.example.order.domain.common.exception.order.InvalidOrderException;
import com.example.order.domain.common.exception.order.StoreNotExistException;
import com.example.order.domain.common.response.MessageConverter;
import com.example.order.domain.common.response.MessageResponse;
import com.example.order.domain.order.controller.model.request.MessageUpdateRequest;
import com.example.order.domain.order.controller.model.request.OrderItemRequest;
import com.example.order.domain.order.controller.model.request.PaymentRequest;
import com.example.order.domain.order.controller.model.response.OrderItemResponse;
import com.example.order.domain.order.controller.model.response.OrderResponse;
import com.example.order.domain.order.controller.model.response.StoreSimpleResponse;
import com.example.order.domain.order.converter.OrderConverter;
import com.example.order.domain.order.controller.model.response.OrderRegisterResponse;
import com.example.order.domain.order.controller.model.request.OrderRegisterRequest;
import com.example.order.domain.order.repository.OrderItem;
import com.example.order.domain.order.repository.Orders;
import com.example.order.domain.order.repository.enums.OrderStatus;
import com.example.order.domain.order.service.ItemFeignClient;
import com.example.order.domain.order.service.OrderItemService;
import com.example.order.domain.order.service.OrderService;
import com.example.order.domain.order.service.StoreFeignClient;
import com.example.order.domain.order.service.model.item.ItemInform;
import com.example.order.domain.order.service.model.item.ItemInternalRequest;
import com.example.order.domain.order.service.model.item.ItemInternalResponse;
import feign.FeignException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Business
@Slf4j
@RequiredArgsConstructor
public class OrderBusiness {

  private final OrderService orderService;
  private final OrderItemService orderItemService;
  private final ItemFeignClient itemFeignClient;
  private final StoreFeignClient storeFeignClient;
  private final OrderConverter orderConverter;
  private final MessageConverter messageConverter;

  public OrderRegisterResponse order(OrderRegisterRequest orderRegisterRequest,
       Long userId) { // 임시 userId
    Orders order = Orders.builder()
        .storeId(orderRegisterRequest.getStoreId())
        .userId(userId) // 임시
        .totalPrice(0L)
        .build();
    orderService.order(order);

    List<OrderItemRequest> orderItemRequests = orderRegisterRequest.
        getOrderItems().stream().toList();

    boolean isValid = createOrderItems(orderItemRequests, order); // order 객체에 orderItem 저장됨.
    if (!isValid) {
      throw new InvalidOrderException(OrderErrorCode.INVALID_ORDER_REQUEST);
    }

    orderService.initPaymentMethod(order, orderRegisterRequest.getPaymentMethod());
    OrderRegisterResponse registerResponse = orderConverter.toRegisterResponse(order);

    return registerResponse;
  }

  private boolean createOrderItems(List<OrderItemRequest> orderItemRequests, Orders order) {
    List<Long> itemIds = orderItemRequests.stream()
        .map(OrderItemRequest::getItemId)
        .toList(); // 상품의 id 리스트

    ItemInternalRequest itemInternalRequest = new ItemInternalRequest(itemIds);
    Api<ItemInternalResponse> response = itemFeignClient.getItem(itemInternalRequest); // 상품의 정보를 불러옴.
    ItemInternalResponse itemInternalResponse = response.result();

    if (itemInternalResponse == null) {
      throw new ItemNotExistFoundException(OrderErrorCode.ITEM_NOT_EXIST);
    }

    for (OrderItemRequest orderItemRequest : orderItemRequests) {
      ItemInform info = itemInternalResponse.getItemInforms().stream()
          .filter(itemInform -> itemInform.getId().equals(orderItemRequest.getItemId()))
          .findFirst()
          .orElseThrow(() -> new InvalidOrderException(OrderErrorCode.INVALID_ORDER_REQUEST));

      if (info.getQuantity() < orderItemRequest.getQuantity()) {
        long available = info.getQuantity();
        long requested = orderItemRequest.getQuantity();

        throw new ItemCountNotEnoughException(
            OrderErrorCode.ITEM_COUNT_NOT_ENOUGH,
            String.format("상품 재고가 부족합니다. 남은 재고: %d개, 요청 수량: %d개", available, requested));
      }

      if (info.getExpiredAt().isBefore(LocalDateTime.now())) {
        throw new ExpiredException(OrderErrorCode.ITEM_EXPIRED);
      }
    }

    for (OrderItemRequest orderItemRequest : orderItemRequests) {
      ItemInform info = itemInternalResponse.getItemInforms().stream()
          .filter(itemInform -> itemInform.getId().equals(orderItemRequest.getItemId()))
          .findFirst()
          .get();

      OrderItem orderItem = OrderItem.builder()
          .quantity(info.getQuantity())
          .price(info.getPrice())
          .totalPrice(info.getPrice() * info.getQuantity())
          .itemId(info.getId())
          .build();

      log.info("{}", orderItem.toString()); // 통신 성공!

      orderService.addOrderItem(order, orderItem);
      orderService.calculateTotalPrice(order);
      orderItemService.save(orderItem);

    }
    return true;
  }

  public MessageResponse completePayment(Long orderId, PaymentRequest paymentRequest, Long userId) { // 임시 userId
    Orders order = orderService.getOrderByOrderIdAndStatus(orderId,
        OrderStatus.PENDING_PAYMENT); // 결제 대기중인 주문 조회

    // 결제 성공(가정)
    orderService.completePayment(order, paymentRequest.isSuccess());

    // 유저 유효성 검사
    if (order.getId() != userId) {
      throw new IllegalArgumentException("USER_NOT_EQUAL");
    }

    List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());
    List<Long> itemIds = orderItems.stream().map(OrderItem::getItemId).toList();
    ItemInternalRequest itemInternalRequest = new ItemInternalRequest(itemIds);
    Api<ItemInternalResponse> response = itemFeignClient.getItem(itemInternalRequest);
    ItemInternalResponse itemInternalResponse = response.result();

    if (itemInternalResponse == null) {
      throw new ItemNotExistFoundException(OrderErrorCode.ITEM_NOT_EXIST);
    }

    Map<Long, ItemInform> itemInformMap = itemInternalResponse.getItemInforms().stream()
        .collect(Collectors.toMap(ItemInform::getId,
            Function.identity()));

    for (OrderItem orderItem : orderItems) {
      ItemInform itemInform = itemInformMap.get(orderItem.getItemId());

      if(itemInform.getQuantity() < orderItem.getQuantity()) {
        long available = itemInform.getQuantity();
        long requested = orderItem.getQuantity();

        throw new ItemCountNotEnoughException(
            OrderErrorCode.ITEM_COUNT_NOT_ENOUGH,
            String.format("상품 재고가 부족합니다. 남은 재고: %d개, 요청 수량: %d개", available, requested));
      }

      if(itemInform.getExpiredAt().isBefore(LocalDateTime.now())) {
        throw new ExpiredException(OrderErrorCode.ITEM_EXPIRED);
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

  // 유저가 주문을 취소
  public MessageResponse cancelOrder(Long orderId, Long userId) {
    Orders order = orderService.getOrderByOrderId(orderId);

    // 유저 유효성 검사
    if(order.getId() != userId) {
      throw new IllegalArgumentException("USER_NOT_EQUAL");
    }

    List<OrderItem> orderItems = orderItemService.findByOrderId(order.getId());

    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest(order.getId(),
        orderItems.stream().map(orderConverter::toOrderItemRequests).toList());

    orderItemService.publishCancelItem(messageUpdateRequest);

    orderService.cancel(order);
    return messageConverter.toResponse("주문이 취소되었습니다.");
  }

  // 스토어에서 주문을 취소
  public MessageResponse cancelStoreOrder(Long orderId, Long userId) {

    try {
      Api<StoreSimpleResponse> storeSimpleResponseApi = storeFeignClient.getStore(userId);
      StoreSimpleResponse response = storeSimpleResponseApi.result();
      Orders order = orderService.getOrderByOrderId(orderId);

      // 유저 유효성 검사
      if (order.getId() != userId) {
        throw new IllegalArgumentException("USER_NOT_EQUAL");
      }
      orderService.cancel(order);
      return messageConverter.toResponse("주문이 취소되었습니다.");

    }
    catch (FeignException e) {
      throw new StoreNotExistException(OrderErrorCode.STORE_NOT_EXIST);
    }

  }

  // 유저의 주문 리스트
  public List<OrderResponse> getOrder(Long userId){
    List<Orders> ordersList = orderService.getOrderListByUserId(userId);
    return getOrderResponses(ordersList);
  }


  // 스토어 점주가 조회하는 주문 리스트
  public List<OrderResponse> getStoreOrder(Long userId) {
    try {
      Api<StoreSimpleResponse> storeSimpleResponseApi = storeFeignClient.getStore(userId);
      StoreSimpleResponse response = storeSimpleResponseApi.result();
      List<Orders> orders = orderService.getOrderListByStoreId(response.getStoreId());
      return getOrderResponses(orders);
    }
    catch (FeignException e) {
      throw new StoreNotExistException(OrderErrorCode.STORE_NOT_EXIST);
    }

  }

  // 유저의 주문 리스트
  private List<OrderResponse> getOrderResponses(List<Orders> ordersList) {

    return ordersList.stream().map(orders ->
    {
      List<OrderItem> orderItems = orderItemService.findByOrderId(orders.getId());
      List<OrderItemResponse> orderItemResponse = orderConverter.toOrderItemResponse(orderItems);

      return OrderResponse.builder()
          .id(orders.getId())
          .orderedAt(orders.getOrderedAt())
          .cancelledAt(orders.getCancelledAt())
          .status(orders.getStatus())
          .totalPrice(orders.getTotalPrice())
          .userId(orders.getUserId())
          .userId(orders.getUserId())
          .orderItemResponses(orderItemResponse)
          .build();
    })
        .toList();

  }

}
