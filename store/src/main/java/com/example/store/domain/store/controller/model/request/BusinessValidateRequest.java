package com.example.store.domain.store.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BusinessValidateRequest {

  @Pattern(regexp="\\d{10}", message="사업자번호는 숫자 10자리")
  private String b_no; // 사업자등록번호 (필수)

  @Pattern(regexp="\\d{8}", message="개업일자는 YYYYMMDD 8자리")
  private String start_dt; // 개업일자 (필수)

  @NotBlank(message = "대표자명")
  private String p_nm; // 대표자성명1: 홍길동 (필수)

}
