package com.example.store.domain.store.scheduler;

import com.example.store.domain.store.business.StoreBusiness;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreStatusUpdateScheduler {

  private final StoreBusiness storeBusiness;

  @Scheduled(cron = "0 * * * * *") // 매 분마다 실행
  @SchedulerLock(name = "storeOperatingStatusUpdateJob")
  public void updateOperatingStatus() {
    //storeBusiness
  }
}
