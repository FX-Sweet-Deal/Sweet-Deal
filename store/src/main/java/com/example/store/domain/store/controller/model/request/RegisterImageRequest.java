package com.example.store.domain.store.controller.model.request;

import com.example.store.domain.store.repository.enums.ImageKind;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterImageRequest {

  private Long itemId;
  private Long storeId;
  private ImageKind imageKind;
  private List<String> serverName;
}
