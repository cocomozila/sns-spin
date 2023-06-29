package spin.sns.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditPasswordParam {

    private String password;
    private String editPassword;
    private String editPasswordCheck;
}
