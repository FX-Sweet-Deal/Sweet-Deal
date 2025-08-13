package com.example.order.domain.order.repository;

import feign.Param;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  /*
  기본 CRUD
   */

  @Query("select oi from OrderItem oi where oi.orderId.id = :orderId")
  List<OrderItem> findByOrderId(@Param("orderId") Long orderId);


}
