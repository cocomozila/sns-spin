package spin.sns.domain.image;

import lombok.NoArgsConstructor;
import spin.sns.domain.post.Post;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

    @NotBlank
    private String imagePath;
    private int sequence;
}
