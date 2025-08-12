package com.example.store.domain.store.controller.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class BusinessValidateResponse {

  @JsonProperty("status_code")
  private String statusCode;
  @JsonProperty("request_cnt")
  private int requestCnt;
  @JsonProperty("valid_cnt")
  private int validCnt;
  private List<Item> data;

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class Item {

    @JsonProperty("b_no")
    private String bNo;
    private String valid;
    @JsonProperty("valid_msg")
    private String validMsg;
    @JsonProperty("request_param")
    private RequestParam requestParam;
    private Status status;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class RequestParam {

    @JsonProperty("b_no")
    private String bNo;
    @JsonProperty("start_dt")
    private String startDt;
    @JsonProperty("p_nm")
    private String pNm;
    @JsonProperty("p_nm2")
    private String pNm2;
    @JsonProperty("b_nm")
    private String bNm;
    @JsonProperty("corp_no")
    private String corpNo;
    @JsonProperty("b_sector")
    private String bSector;
    @JsonProperty("b_type")
    private String bType;
    @JsonProperty("b_adr")
    private String bAdr;

  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  @Getter
  public static class Status {

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
}
