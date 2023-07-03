package spin.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spin.sns.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);
}
