package com.example.store.domain.store.controller.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BusinessStatusResponse(
    @JsonProperty("status_code") String statusCode,
    @JsonProperty("match_cnt")   int matchCnt,
    @JsonProperty("request_cnt") int requestCnt,
    List<Info> data
) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Info(
      @JsonProperty("b_no")               String bNo,
      @JsonProperty("b_stt")              String bStt,
      @JsonProperty("b_stt_cd")           String bSttCd,
      @JsonProperty("tax_type")           String taxType,
      @JsonProperty("tax_type_cd")        String taxTypeCd,
      @JsonProperty("end_dt")             String endDt,
      @JsonProperty("utcc_yn")            String utccYn,
      @JsonProperty("tax_type_change_dt") String taxTypeChangeDt,
      @JsonProperty("invoice_apply_dt")   String invoiceApplyDt,
      @JsonProperty("rbf_tax_type")       String rbfTaxType,
      @JsonProperty("rbf_tax_type_cd")    String rbfTaxTypeCd
  ) {}
}
