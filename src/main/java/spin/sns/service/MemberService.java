package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spin.sns.domain.member.Member;
import spin.sns.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void signup(Member member) {
        memberRepository.save(member);
    }

    public String findAccount(String email) {
        return memberRepository
                .findAccount(email)
                .orElseThrow(RuntimeException::new)
                .getNickname();
    }
}
