package account.domain.account.controller.model;

import com.example.user.domain.user.repository.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenValidationResponse {


    private Long userId;

    private String email;

    private UserRole role;


}
