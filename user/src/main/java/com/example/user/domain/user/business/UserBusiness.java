package com.example.user.domain.user.business;


import com.example.global.anntation.Business;
import com.example.user.domain.common.response.MessageConverter;
import com.example.user.domain.common.response.MessageResponse;
import com.example.user.domain.jwt.business.TokenBusiness;
import com.example.user.domain.jwt.model.TokenResponse;
import com.example.user.domain.user.controller.model.login.UserLoginRequest;
import com.example.user.domain.user.controller.model.login.UserResponse;
import com.example.user.domain.user.controller.model.register.UserRegisterRequest;
import com.example.user.domain.user.converter.UserConverter;
import com.example.user.domain.user.repository.UserEntity;
import com.example.user.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Business
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;
    private final MessageConverter messageConverter;
    private final TokenBusiness tokenBusiness;
    

    public MessageResponse register(UserRegisterRequest userRegisterRequest) {

        userService.existsByEmailWithThrow(userRegisterRequest.getEmail());
        userService.existsByNameWithThrow(userRegisterRequest.getName());

        UserEntity userEntity = userConverter.toEntity(userRegisterRequest);
        userService.register(userEntity);

        return messageConverter.toResponse("회원가입이 완료되었습니다.");

    }

    public TokenResponse login(UserLoginRequest UserLoginRequest) {
        UserEntity userEntity = userService.login(UserLoginRequest);
        return tokenBusiness.issueToken(userEntity);

    }

    public UserResponse getUserInformation(Long userId) {
        UserEntity userEntity = userService.getUserWithThrow(userId);
        return userConverter.toResponse(userEntity);

    }

    public MessageResponse unregister(Long userId) {
        userService.unregister(userId);
        return messageConverter.toResponse("회원탈퇴가 완료되었습니다.");

    }
}
