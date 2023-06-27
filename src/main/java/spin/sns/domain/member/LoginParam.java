package spin.sns.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginParam {

    private String nickname;
    private String password;
}
