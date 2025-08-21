package com.example.user.domain.user.controller;


import com.example.global.api.Api;
import com.example.user.domain.common.response.MessageResponse;
import com.example.user.domain.jwt.model.TokenResponse;
import com.example.user.domain.user.business.UserBusiness;
import com.example.user.domain.user.controller.model.login.UserLoginRequest;
import com.example.user.domain.user.controller.model.register.UserRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/user")
public class UserOpenApiController {

    private final UserBusiness userBusiness;

    @PostMapping()
    @Operation(summary = "[회원가입]")
    public Api<MessageResponse> register(
        @RequestBody
        @Valid Api<UserRegisterRequest> userRegisterRequest
    ) {
        MessageResponse response = userBusiness.register(userRegisterRequest.result());
        return Api.ok(response);
    }


    @PostMapping("/login")
    @Operation(summary = "[로그인]")
    public Api<TokenResponse> login(
        @RequestBody @Valid Api<UserLoginRequest> userLoginRequest) {

        TokenResponse response = userBusiness.login(userLoginRequest.result());
        return Api.ok(response);
    }
}
