package com.example.store.domain.store.repository;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.Data;

@Embeddable
@Data
public class OperatingTime {
  private LocalTime openingTime;
  private LocalTime closingTime;
  private boolean openAllDay; // 24 hours
}
