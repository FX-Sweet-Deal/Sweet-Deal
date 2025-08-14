package com.example.store.domain.store.controller.model.response.openApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class BusinessValidateResponse {

  @JsonProperty("status_code")
  private String statusCode;
  @JsonProperty("request_cnt")
  private int requestCnt;
  @JsonProperty("valid_cnt")
  private int validCnt;


}
