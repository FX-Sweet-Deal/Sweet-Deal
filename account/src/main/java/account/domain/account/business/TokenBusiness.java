package account.domain.account.business;

import com.example.user.domain.user.repository.UserRepository;
import account.domain.account.controller.model.TokenValidationResponse;
import account.domain.account.model.TokenDto;
import account.domain.account.service.TokenService;
import com.example.global.anntation.Business;
import com.example.global.errorcode.UserErrorCode;
import com.example.user.common.exception.user.UserNotFoundException;
import com.example.user.domain.user.repository.UserEntity;
import com.example.user.domain.user.repository.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Business
@Slf4j
@RequiredArgsConstructor
public class TokenBusiness {


    private final TokenService tokenService;
    private final UserRepository userRepository;

    public TokenValidationResponse tokenValidation(TokenDto tokenDto) {

        Long userId = tokenService.validationToken(tokenDto.getToken().substring(7));

        UserEntity userEntity = userRepository.findFirstByIdAndStatusOrderByIdDesc(userId, UserStatus.REGISTERED)
            .orElseThrow(() -> new UserNotFoundException(UserErrorCode.USER_NOT_FOUND));

        return TokenValidationResponse.builder()
            .userId(userEntity.getId())
            .email(userEntity.getEmail())
            .role(userEntity.getRole())
            .build();
    }
}
