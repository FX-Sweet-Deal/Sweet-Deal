package com.example.store.domain.store.controller.model.response.openApi;

import lombok.Getter;

@Getter
public class InfoResponse {
    private String b_no;

    private String valid;

    private String valid_msg;

    private RequestParamResponse request_param;

    private StatusResponse status;
}
