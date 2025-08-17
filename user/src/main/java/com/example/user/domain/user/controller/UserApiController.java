package com.example.user.domain.user.controller;


import com.example.global.api.Api;
import com.example.user.domain.jwt.model.TokenClaimsData;
import com.example.user.domain.user.business.UserBusiness;
import com.example.user.domain.user.controller.model.login.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    private final UserBusiness userBusiness;

    @GetMapping("/me")
    @Operation(summary = "[회원 정보 조회]")
    public Api<UserResponse> getUserInformation() {
        var attrs = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        Object attr = attrs.getAttribute("userId", RequestAttributes.SCOPE_REQUEST);
        Long userId;

        if (attr instanceof Long l) {
            userId = l;
        } else if (attr instanceof String s) {
            userId = Long.parseLong(s);
        } else if (attr instanceof TokenClaimsData t) {
            userId = t.getUserId(); // or t.userId()
        } else {
            throw new IllegalStateException(
                "userId attribute invalid type: " + (attr == null ? "null"
                    : attr.getClass().getName()));
        }

        var response = userBusiness.getUserInformation(userId);
        return Api.ok(response);
    }

    
}
