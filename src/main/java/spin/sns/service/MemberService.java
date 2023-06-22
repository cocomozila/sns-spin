package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spin.sns.domain.member.LoginParam;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.DuplicateEmailException;
import spin.sns.error.exception.DuplicateNicknameException;
import spin.sns.error.exception.MemberNotExistException;
import spin.sns.error.exception.PasswordMismatchException;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;

    public void signup(Member member) {
        if (memberRepository.findByNickname(member.getNickname()).isPresent()) {
            throw new DuplicateNicknameException("사용중인 닉네임입니다.");
        }
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new DuplicateEmailException("사용중인 이메일입니다.");
        }
        memberRepository.save(member);
    }

    public String findAccount(String email) {
        return memberRepository
                .findByEmail(email)
                .orElseThrow(() -> new MemberNotExistException("사용자를 찾을 수 없습니다."))
                .getNickname();
    }

    public Member getLoginMember(LoginParam loginParam, HttpServletResponse response) {
        Member member = memberRepository
                .findByNickname(loginParam.getNickname())
                .orElseThrow(() -> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        if (member.getPassword().equals(loginParam.getPassword())) {
            sessionRepository.createSession(member, response);
            return member;
        }
        throw new PasswordMismatchException("패스워드가 일치하지 않습니다.");
    }
}
