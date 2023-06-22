package spin.sns.repository;

import spin.sns.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findAccount(String email);

    Optional<Member> findMemberByNickname(String nickname);
}
