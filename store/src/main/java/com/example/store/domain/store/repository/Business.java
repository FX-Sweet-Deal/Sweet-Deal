package com.example.store.domain.store.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Getter
@NoArgsConstructor @AllArgsConstructor
@Builder @Jacksonized
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Business {

  @Pattern(regexp="\\d{10}", message="사업자번호는 숫자 10자리")
  @JsonProperty("b_no")
  private String b_no; // 사업자등록번호 (필수)

  @Pattern(regexp="\\d{8}", message="개업일자는 YYYYMMDD 8자리")
  @JsonProperty("start_dt")
  private String start_dt; // 개업일자 (필수)

  @NotBlank(message = "대표자명")
  @JsonProperty("p_nm")
  private String p_nm; // 대표자성명1: 홍길동 (필수)

  @JsonProperty("p_nm2")
  private String p_nm2; // 대표자성명2 - 대표자성명1이 한글이 아닌 경우, 이에 대한 한글명 (선택)

  @JsonProperty("b_nm")
  private String b_nm; // 상호명: (주)테스트 (선택)

  @JsonProperty("corp_no")
  private String corp_no; // 법인등록번호 (선택)

  @JsonProperty("b-sector")
  private String b_sector; // 주업태명 (선택)

  @JsonProperty("b_type")
  private String b_type; // 주종목명 (선택)

  @JsonProperty("b_adr")
  private String b_adr; // 사업자주소 (선택)

}