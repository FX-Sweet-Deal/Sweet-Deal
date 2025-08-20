package account.domain.account.ifs;

import java.util.Map;

public interface TokenHelperIfs {

    Map<String, Object> validationTokenWithThrow(String token);


}
