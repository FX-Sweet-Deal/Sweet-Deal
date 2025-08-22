package account.domain.account.controller;

import account.domain.account.business.TokenBusiness;
import account.domain.account.controller.model.TokenValidationRequest;
import account.domain.account.controller.model.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/account")
public class AccountApiController {

    private final TokenBusiness tokenBusiness;

    @PostMapping()
    public TokenValidationResponse validationToken(
        @RequestBody TokenValidationRequest request
    ) {
        log.info("123");
        TokenValidationResponse response = tokenBusiness.tokenValidation(request.getTokenDto());
        return response;

    }
}
