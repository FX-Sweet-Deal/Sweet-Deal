package com.example.user.domain.user.controller;


import com.example.global.anntation.UserSession;
import com.example.global.api.Api;
import com.example.user.domain.common.resolver.User;
import com.example.user.domain.user.business.UserBusiness;
import com.example.user.domain.user.controller.model.login.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserBusiness userBusiness;

    @GetMapping()
    @Operation(summary = "[회원 정보 조회]")
    public Api<UserResponse> getUserInformation(@Parameter(hidden = true) @UserSession User user) {


        log.info("user= {}", user);

        UserResponse response = userBusiness.getUserInformation(user.getId());
        return Api.ok(response);

    }

    
}
