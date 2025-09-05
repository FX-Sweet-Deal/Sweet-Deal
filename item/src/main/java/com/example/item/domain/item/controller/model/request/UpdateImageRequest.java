package com.example.item.domain.item.controller.model.request;


import com.example.item.domain.item.repository.enums.ImageKind;
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
