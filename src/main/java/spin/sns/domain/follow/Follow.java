package spin.sns.domain.follow;

import lombok.NoArgsConstructor;
import spin.sns.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", insertable = false, updatable = false)
    private Member followMember;

}
