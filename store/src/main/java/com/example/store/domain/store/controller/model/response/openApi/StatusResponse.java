package com.example.store.domain.store.controller.model.response.openApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class StatusResponse {

  @JsonProperty("b_no")
  private String bNo;
  @JsonProperty("b_stt")
  private String bStt;
  @JsonProperty("b_stt_cd")
  private String bSttCd;
  @JsonProperty("tax_type")
  private String taxType;
  @JsonProperty("tax_type_cd")
  private String taxTypeCd;
  @JsonProperty("end_dt")
  private String endDt;
  @JsonProperty("utcc_yn")
  private String utccYn;
  @JsonProperty("tax_type_change_dt")
  private String taxTypeChangeDt;
  @JsonProperty("invoice_apply_dt")
  private String invoiceApplyDt;
  @JsonProperty("rbf_tax_type")
  private String rbfTaxType;
  @JsonProperty("rbf_tax_type_cd")
  private String rbfTaxTypeCd;

}