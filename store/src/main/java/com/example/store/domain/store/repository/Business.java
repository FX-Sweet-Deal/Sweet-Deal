package com.example.store.domain.store.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Business {

  @Pattern(regexp="\\d{10}", message="사업자번호는 숫자 10자리")
  private String b_no; // 사업자등록번호 (필수)

  @Pattern(regexp="\\d{8}", message="개업일자는 YYYYMMDD 8자리")
  private String start_dt; // 개업일자 (필수)

  @NotBlank(message = "대표자명")
  private String p_nm; // 대표자성명1: 홍길동 (필수)

  private String p_nm2; // 대표자성명2 - 대표자성명1이 한글이 아닌 경우, 이에 대한 한글명 (선택)

  private String b_nm; // 상호명: (주)테스트 (선택)

  private String corp_no; // 법인등록번호 (선택)

  private String b_sector; // 주업태명 (선택)

  private String b_type; // 주종목명 (선택)

  private String b_adr; // 사업자주소 (선택)

}