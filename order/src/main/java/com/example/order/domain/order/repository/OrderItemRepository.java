package com.example.order.domain.order.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  /*
  기본 CRUD
   */

  List<OrderItem> findByOrderId(Long orderId);


}
