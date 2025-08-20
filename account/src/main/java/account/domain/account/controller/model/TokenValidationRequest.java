package account.domain.account.controller.model;

import account.domain.account.model.TokenDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValidationRequest {


    private TokenDto tokenDto;


}
