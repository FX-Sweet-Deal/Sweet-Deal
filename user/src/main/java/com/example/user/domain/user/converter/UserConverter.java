package com.example.user.domain.user.converter;


import com.example.global.anntation.Converter;
import com.example.global.errorcode.ErrorCode;
import com.example.user.domain.user.controller.model.login.UserResponse;
import com.example.user.domain.user.controller.model.register.UserRegisterRequest;
import com.example.user.domain.user.repository.UserEntity;
import com.example.user.domain.user.repository.enums.UserRole;
import com.example.user.domain.user.repository.enums.UserStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ApiException;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Converter
@RequiredArgsConstructor
public class UserConverter {


    public UserEntity toEntity(UserRegisterRequest userRegisterRequest) {
        return  UserEntity.builder()
            .email(userRegisterRequest.getEmail())
            .password(BCrypt.hashpw(userRegisterRequest.getPassword(), BCrypt.gensalt()))
            .name(userRegisterRequest.getName())
            .birth(userRegisterRequest.getBirth())
            .address(userRegisterRequest.getAddress())
            .phone(userRegisterRequest.getPhone())
            .role(UserRole.USER)
            .build();
    }

    public UserResponse toResponse(UserEntity userEntity) {
        return UserResponse.builder()
            .id(userEntity.getId())
            .email(userEntity.getEmail())
            .name(userEntity.getName())
            .birth(userEntity.getBirth())
            .address(userEntity.getAddress())
            .phone(userEntity.getPhone())
            .role(userEntity.getRole())
            .build();
    }

}
