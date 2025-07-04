package com.example.item.domain.item.scheduler;

import com.example.item.domain.item.business.ItemBusiness;
import com.example.item.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpiredItemScheduler {

  private final ItemService itemService;

  @Scheduled(cron = "0 0 0 * * ?") // 매일 00시에 실행
  public void deleteItem30Day() {
    itemService.deleteExpiredSoldItems();

  }
}
