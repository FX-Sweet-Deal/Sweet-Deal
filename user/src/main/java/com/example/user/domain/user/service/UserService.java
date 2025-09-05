package com.example.user.domain.user.service;


import com.example.global.errorCode.UserErrorCode;
import com.example.global.exception.BadRequestException;
import com.example.global.exception.ConflictException;
import com.example.global.exception.NotFoundException;
import com.example.global.exception.UserNotFoundException;
import com.example.user.domain.common.exception.user.ExistUserEmailException;
import com.example.user.domain.common.exception.user.ExistUserNameException;
import com.example.user.domain.common.exception.user.LoginFailException;
import com.example.user.domain.common.exception.user.UserUnregisterException;
import com.example.user.domain.user.controller.model.login.UserLoginRequest;
import com.example.user.domain.user.controller.model.update.UserPasswordChangeRequest;
import com.example.user.domain.user.controller.model.update.UserUpdateRequest;
import com.example.user.domain.user.repository.UserEntity;
import com.example.user.domain.user.repository.UserRepository;
import com.example.user.domain.user.repository.enums.UserStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;

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

    public UserEntity getUserWithThrow(
        Long userId
    ) {
        log.info("userId={}", userId);

        return userRepository.findFirstByIdAndStatusOrderByIdDesc(
            userId,
            UserStatus.REGISTERED
        ).orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

    }



    private void saveFcmToken(Long userId, String fcmToken) {
        redisTemplate.opsForHash().put(String.valueOf(userId), "fcmToken", fcmToken);
    }

    public void unregister(Long userId) {
        UserEntity userEntity = userRepository.findFirstByIdAndStatusOrderByIdDesc(userId,
            UserStatus.REGISTERED)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));
        userEntity.setStatus(UserStatus.UNREGISTERED);
        userEntity.setUnregisteredAt(LocalDateTime.now());
        UserEntity unRegisterdUSErEntity = userRepository.save(userEntity);
        if(!unRegisterdUSErEntity.getStatus().equals(UserStatus.UNREGISTERED)) {
            throw new UserUnregisterException(UserErrorCode.USER_UNREGISTER_FAIL);
        }
    }

    public UserEntity updateUser(Long userId, @Valid UserUpdateRequest request) {

        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."));

        // name
        if (request.getName() != null) {
            user.changeName(request.getName().trim());
        }

        // birth
        if (request.getBirth() != null) {
            user.changeBirth(request.getBirth());
        }

        // address (DTO에 @NotBlank 있어 '보내면' 반드시 값이 있어야 함. null이면 무시)
        if (request.getAddress() != null) {
            String addr = request.getAddress().trim();
            if (addr.isEmpty()) {
                throw new BadRequestException("qwe", "주소는 공백일 수 없습니다.");
            }
            user.changeAddress(addr);
        }

        // phone (중복/유니크 체크)
        if (request.getPhone() != null) {
            String newPhone = request.getPhone().trim();
            if (userRepository.existsByPhoneAndIdNot(newPhone, user.getId())) {
                throw new ConflictException("PHONE_DUPLICATED", "이미 사용 중인 전화번호입니다.");
            }
            user.changePhone(newPhone);
        }
        // JPA 영속 상태라 변경 감지로 업데이트됨
        userRepository.flush();

        return user;

    }



    @Transactional
    public void changePassword(Long userId, @Valid UserPasswordChangeRequest request) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("USER NOT FOUND", "사용자를 찾을 수 없습니다."));

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 암호화 후 저장
        user.changePassword(passwordEncoder.encode(request.getNewPassword()));
        // userRepository.save(user); // @Transactional이면 생략 가능
    }
}
