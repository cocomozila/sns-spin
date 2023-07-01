package spin.sns.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spin.sns.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private LocalDateTime createTime;
    private String content;
    private int likeCount;

    @Builder
    public Post(Member member, LocalDateTime createTime, String content) {
        this.member = member;
        this.createTime = LocalDateTime.now();
        this.content = content;
    }
}
