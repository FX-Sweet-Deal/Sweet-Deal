package com.example.store.domain.store.controller.model.request;

import com.example.store.domain.store.repository.Address;
import com.example.store.domain.store.repository.OperatingTime;
import com.example.store.domain.store.repository.enums.StoreCategory;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateRequest {

  @Pattern(
      regexp = "^[가-힣A-Za-z0-9 ]{1,100}$",
      message = "한글, 영문, 숫자 및 공백만 입력 가능합니다."
  )
  private String name;

  private Address address;

  @Size(min = 11, max = 11, message = "전화번호는 숫자 11자리여야 합니다.")
  private String phone;

  @Size(min = 10, max = 10, message = "사업자등록번호는 숫자 10자리여야 합니다.")
  private String businessNumber;

  private StoreCategory category;

  private OperatingTime operatingTime;

}
