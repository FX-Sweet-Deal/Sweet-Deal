package com.example.user.domain.jwt.controller;


import com.example.global.api.Api;
import com.example.user.domain.jwt.business.TokenBusiness;
import com.example.user.domain.jwt.model.TokenDto;
import com.example.user.domain.jwt.model.TokenValidationRequest;
import com.example.user.domain.jwt.model.TokenValidationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/token")
public class JwtOpenApiController {


    private final TokenBusiness tokenBusiness;

    @PostMapping("/reissue")
    @Operation(summary = "[refreshToken 으로 accessToken 재발급]")
    public Api<TokenDto> reIssueAccessToken(

        @Parameter(hidden = true) @RequestHeader("Authorization") String refreshToken
    ) {
        TokenDto response = tokenBusiness.reIssueAccessToken(refreshToken);
        return Api.ok(response);
    }

}
