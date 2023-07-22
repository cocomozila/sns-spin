package spin.sns.domain.follow;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spin.sns.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "FOLLOW_MEMBER_ID")
    private Member followMember;

    @Builder
    public Follow(Member member, Member followMember) {
        this.member = member;
        this.followMember = followMember;
    }
}
