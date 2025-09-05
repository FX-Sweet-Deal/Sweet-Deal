package com.example.image.domain.image.controller.model;

import com.example.image.domain.image.repository.enums.ImageKind;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateImageRequest {

  private Long itemId;
  private Long storeId;
  private ImageKind imageKind;
  private List<String> serverNames;

}
