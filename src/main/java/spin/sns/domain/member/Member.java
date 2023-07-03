package spin.sns.domain.member;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import spin.sns.domain.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    private int followers;
    private int following;

    @Builder
    public Member(String nickname, String password, String email, String introduceContext) {
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.introduceContext = introduceContext;
    }

    public boolean isValidatedEditPassword(String editPassword) {
        return this.getPassword().equals(editPassword);
    }
}
