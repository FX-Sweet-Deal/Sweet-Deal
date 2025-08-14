package com.example.store.domain.store.controller.model.response.openApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Getter;

@Getter
@JsonPropertyOrder({ "status_code", "request_cnt", "valid_cnt", "data" }) //  순서 지정
public class BusinessValidateResponse {

  @JsonProperty("status_code")
  private String statusCode;
  @JsonProperty("request_cnt")
  private int requestCnt;
  @JsonProperty("valid_cnt")
  private int validCnt;

  private List<InfoResponse> data;

}
