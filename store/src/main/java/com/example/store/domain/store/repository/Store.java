package com.example.store.domain.store.repository;

import com.example.store.domain.store.repository.enums.OperatingStatus;
import com.example.store.domain.store.repository.enums.StoreCategory;
import com.example.store.domain.store.repository.enums.StoreStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Store {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id", nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  @Embedded
  private Address address;

  @Column(nullable = false)
  private String phone;

  @Column(name = "business_number", nullable = false)
  private String businessNumber;

  @Enumerated
  @Column(nullable = false)
  private StoreCategory category;

  @Enumerated
  @Column(nullable = false)
  private OperatingStatus operatingStatus;

  @Enumerated
  @Column(nullable = false)
  private StoreStatus StoreStatus;

  @Column(nullable = false)
  private LocalDateTime registeredAt;

  @Column(nullable = false)
  private LocalDateTime unregisteredAt;
  @Column(name = "user_id", nullable = false)
  private Long userId;
}
