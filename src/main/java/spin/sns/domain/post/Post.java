package spin.sns.domain.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spin.sns.domain.image.Image;
import spin.sns.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @Builder
    public Post(Member member, String content) {
        this.member = member;
        this.createTime = LocalDateTime.now();
        this.content = content;
    }

    public boolean isSameAuthors(Member member) {
        return this.member.getMemberId() == member.getMemberId();
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
