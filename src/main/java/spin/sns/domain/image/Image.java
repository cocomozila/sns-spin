package spin.sns.domain.image;

import lombok.Builder;
import lombok.NoArgsConstructor;
import spin.sns.domain.post.Post;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    private String serverFilename;
    private String originalFilename;

    @Builder
    public Image(Post post, String serverFilename, String originalFilename) {
        this.post = post;
        this.serverFilename = serverFilename;
        this.originalFilename = originalFilename;
    }
}
