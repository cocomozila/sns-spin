package spin.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spin.sns.domain.follow.Follow;
import spin.sns.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowCustomRepository {

    Optional<Follow> findByMemberEqualsAndFollowMemberEquals(Member member, Member followMember);

    List<Follow> findAllByFollowMember(Member member);
}
