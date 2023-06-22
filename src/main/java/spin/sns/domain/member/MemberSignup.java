package spin.sns.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
public class MemberSignup {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String nickname;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    private String email;

    private String introduceContext;

    public MemberSignup(String nickname, String password, String email, String introduceContext) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.introduceContext = introduceContext;
    }

    public Member createEntity() {
        return Member.builder()
                .nickname(this.nickname)
                .password(this.password)
                .email(this.email)
                .introduceContext(this.introduceContext)
                .build();
    }
}
