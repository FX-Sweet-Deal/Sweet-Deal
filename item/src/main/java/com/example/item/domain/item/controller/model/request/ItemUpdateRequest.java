package com.example.item.domain.item.controller.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

  @NotNull
  @Pattern(
      regexp = "^[가-힣A-Za-z0-9]{1,100}$",
      message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
  )
  private String name;

//  @Enumerated(EnumType.STRING)
//  private ItemStatus status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
  @FutureOrPresent(message = "지정된 일시는 현재 또는 미래여야 합니다.")
  private LocalDateTime expiredAt;

  @Pattern(regexp = "^[0-9]$", message = "숫자만 입력할 수 있습니다.")
  private Long price;

  @Pattern(regexp = "^[0-9]$", message = "숫자만 입력할 수 있습니다.")
  private Long quantity;

  @NotNull
  private List<String> serverName;

}
