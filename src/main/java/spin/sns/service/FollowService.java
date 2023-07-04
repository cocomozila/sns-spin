package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.follow.Follow;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.DuplicateFollowException;
import spin.sns.error.exception.MemberNotExistException;
import spin.sns.repository.FollowRepository;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final FollowRepository followRepository;

    @Transactional
    public void following(String userNickname, HttpServletRequest request) {
        Member loginMember = sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow();

        Member followMember = memberRepository.findByNickname(userNickname)
                .orElseThrow(()-> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        Follow follow = Follow.builder()
                        .member(findMember)
                        .followMember(followMember)
                        .build();

        Optional<Follow> findFollow = followRepository
                .findByMemberEqualsAndFollowMemberEquals(findMember, followMember);

        if (findFollow.isPresent()) {
            throw new DuplicateFollowException("이미 팔로우 했습니다.");
        }
        followRepository.save(follow);
    }
}
