package com.example.order.domain.order.repository;

import com.example.order.domain.order.repository.enums.OrderStatus;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

  /*
  기본 CRUD
   */

  Optional<Orders> findByIdAndStatus(Long id, OrderStatus status);

  List<Orders> findByUserId(Long userId);

  List<Orders> findByStoreIdIn(List<Long> storeIds);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT o FROM Orders o where o.id = :orderId")
  Optional<Orders> findByIdPessimisticLock(Long orderId);
}
