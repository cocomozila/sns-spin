package spin.sns.repository;

import spin.sns.domain.member.EditPasswordParam;
import spin.sns.domain.member.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    void deleteMember(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    void editPassword(EditPasswordParam editPasswordParam, String nickname);
}
