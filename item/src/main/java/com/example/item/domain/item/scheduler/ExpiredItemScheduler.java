package com.example.item.domain.item.scheduler;

import com.example.item.domain.item.business.ItemBusiness;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredItemScheduler {

  private final ItemBusiness itemBusiness;

  @Scheduled(cron = "0 0 0 * * ?") // 매일 00시에 실행
  public void deleteItem30Day() {
    itemBusiness.deleteExpiredSoldItems();

  }

  @Scheduled(cron = "0 0 0 * * ?") // 매일 00시에 실행
  public void deleteItemExpiredAtOver() {
    itemBusiness.deleteExpiredAtOver();
  }
}
