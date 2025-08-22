package com.example.store.domain.store.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.Data;

@Embeddable
@Data
public class OperatingTime {
  private LocalTime openingTime;
  private LocalTime closingTime;
  @Column(name = "open_all_day", columnDefinition = "TINYINT(1)")
  private boolean openAllDay; // 24 hours
}
