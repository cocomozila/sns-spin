package spin.sns.domain.postlike;

import lombok.NoArgsConstructor;
import spin.sns.domain.post.Post;
import spin.sns.domain.member.Member;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
