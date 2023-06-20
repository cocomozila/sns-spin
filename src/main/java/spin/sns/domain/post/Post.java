package spin.sns.domain.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import spin.sns.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
    private LocalDateTime createTime;
    private String content;
    private int likeCount;
}
