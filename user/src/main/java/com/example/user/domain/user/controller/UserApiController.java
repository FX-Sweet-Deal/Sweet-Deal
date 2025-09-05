package com.example.user.domain.user.controller;


import com.example.global.annotation.UserSession;
import com.example.global.api.Api;
import com.example.global.resolver.User;
import com.example.user.domain.common.response.MessageResponse;
import com.example.user.domain.user.business.UserBusiness;
import com.example.user.domain.user.controller.model.login.UserResponse;
import com.example.user.domain.user.controller.model.update.UserPasswordChangeRequest;
import com.example.user.domain.user.controller.model.update.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping()
    @Operation(summary = "[회원 탈퇴]")
    public Api<MessageResponse> unregister(@Parameter(hidden = true) @UserSession User user) {
        MessageResponse response = userBusiness.unregister(user.getId());
        return Api.ok(response);
    }


    @PutMapping()
    @Operation(summary = "[회원 정보 수정]")
    public Api<UserResponse> updateUser(
        @Parameter(hidden = true) @UserSession User user,
        @Valid @RequestBody UserUpdateRequest request
    ) {
        UserResponse response = userBusiness.updateUser(user.getId(), request);
        return Api.ok(response);

    }

    @PostMapping("/password")
    @Operation(summary = "[비밀번호 변경]")
    public Api<MessageResponse> changePassword(
        @Parameter(hidden = true) @UserSession User userSession,
        @Valid @RequestBody UserPasswordChangeRequest request
    ){
        MessageResponse response = userBusiness.changePassword(userSession.getId(), request);
        return Api.ok(response);
    }
}
