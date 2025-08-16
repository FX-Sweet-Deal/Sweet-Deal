package com.example.user.domain.user.service;


import com.example.global.errorcode.UserErrorCode;
import com.example.user.common.exception.user.ExistUserEmailException;
import com.example.user.common.exception.user.ExistUserNameException;
import com.example.user.common.exception.user.LoginFailException;
import com.example.user.common.exception.user.UserNotFoundException;
import com.example.user.domain.user.controller.model.login.UserLoginRequest;
import com.example.user.domain.user.repository.UserEntity;
import com.example.user.domain.user.repository.UserRepository;
import com.example.user.domain.user.repository.enums.UserStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    public void register(UserEntity userEntity) {
        userEntity.setStatus(UserStatus.REGISTERED);
        userEntity.setRegisteredAt(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    private void existsWithThrow(boolean exists, RuntimeException exception) {
        if (exists) {
            throw exception;
        }
    }

    public void existsByNameWithThrow(String name) {
        existsWithThrow(
            userRepository.existsByName(name),
            new ExistUserNameException(UserErrorCode.EXISTS_USER_NAME)
        );
    }

    public void existsByEmailWithThrow(String email) {
        existsWithThrow(
            userRepository.existsByEmail(email),
            new ExistUserEmailException(UserErrorCode.EXISTS_USER_EMAIL)
        );
    }

    public UserEntity login(UserLoginRequest userLoginRequest) {

        // UNREGISTERED 가 아닌 UserEntity 반환
        UserEntity userEntity = userRepository.findFirstByEmailAndStatusNotOrderByEmailDesc(
                userLoginRequest.getEmail(), UserStatus.UNREGISTERED)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        if (BCrypt.checkpw(userLoginRequest.getPassword(), userEntity.getPassword())) {
            userEntity.setLastLoginAt(LocalDateTime.now());
            userRepository.save(userEntity);
            return userEntity;
        }

        throw new LoginFailException(UserErrorCode.LOGIN_FAIL);
    }



    private void saveFcmToken(Long userId, String fcmToken) {
        redisTemplate.opsForHash().put(String.valueOf(userId), "fcmToken", fcmToken);
    }
}
