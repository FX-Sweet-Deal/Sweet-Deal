package com.example.order.domain.order.service.model.item;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ItemInternalResponse {
  private List<ItemInform> itemInforms;

}
