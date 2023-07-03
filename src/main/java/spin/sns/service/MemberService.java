package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spin.sns.domain.member.EditPasswordParam;
import spin.sns.domain.member.FindPasswordParam;
import spin.sns.domain.member.LoginParam;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.*;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
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

    public void logout(HttpServletRequest request) {
        sessionRepository.expire(request);
    }

    public String findPassword(FindPasswordParam findPasswordParam) {
        Member findMember = memberRepository
                .findByNickname(findPasswordParam.getNickname())
                .orElseThrow(() -> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        if (findMember.getEmail().equalsIgnoreCase(findPasswordParam.getEmail())) {
            return findMember.getPassword();
        }
        throw new MemberNotExistException("사용자를 찾을 수 없습니다.");
    }

    public void editPassword(EditPasswordParam editPasswordParam, HttpServletRequest request) {
        Member member = (Member) sessionRepository.getSession(request);

        if (member.isValidatedEditPassword(editPasswordParam.getPassword())) {
            if (editPasswordParam.getEditPassword().equals(editPasswordParam.getEditPasswordCheck())) {
                memberRepository.editPassword(editPasswordParam, member.getNickname());
                return;
            }
            throw new PasswordConfirmationMismatchException("변경할 비밀번호가 일치하지 않습니다.");
        }
        throw new PasswordMismatchException("패스워드가 일치하지 않습니다.");
    }

    public void deleteAccount(HttpServletRequest request) {
        Member member = (Member) sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(member.getMemberId())
                .orElseThrow();
        memberRepository.delete(findMember);
    }
}
