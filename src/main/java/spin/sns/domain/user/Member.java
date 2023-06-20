package spin.sns.domain.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    public Member(String userId, String password, String email, String introduceContext) {
        this.nickname = userId;
        this.password = password;
        this.email = email;
        this.introduceContext = introduceContext;
    }
}
