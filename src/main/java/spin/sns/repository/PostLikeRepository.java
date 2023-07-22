package spin.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spin.sns.domain.member.Member;
import spin.sns.domain.post.Post;
import spin.sns.domain.postlike.PostLike;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByMemberAndPost(Member member, Post post);
}
