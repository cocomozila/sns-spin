package spin.sns.repository;

import spin.sns.domain.post.Post;

public interface PostRepository {

    Post uploadPost(Post post);
}
