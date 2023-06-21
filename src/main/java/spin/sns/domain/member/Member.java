package spin.sns.domain.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
@Data
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @NotBlank
    private String email;
    private String introduceContext;

    @Builder
    public Member(String nickname, String password, String email, String introduceContext) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.introduceContext = introduceContext;
    }
}
