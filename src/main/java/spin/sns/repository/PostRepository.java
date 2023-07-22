package spin.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spin.sns.domain.post.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
