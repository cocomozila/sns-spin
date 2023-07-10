package spin.sns.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.follow.Follow;
import spin.sns.domain.follow.Followers;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.DuplicateFollowException;
import spin.sns.error.exception.MemberNotExistException;
import spin.sns.repository.FollowRepository;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class FollowService {

    private final int FOLLOWER = 0;
    private final int FOLLOWING = 1;

    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final FollowRepository followRepository;

    @Transactional
    public void following(String userNickname, HttpServletRequest request) {
        List<Member> followParam = getFollowerAndFollowing(userNickname, request);
        Optional<Follow> findFollow = followRepository
                .findByMemberEqualsAndFollowMemberEquals(
                        followParam.get(FOLLOWER), followParam.get(FOLLOWING));

        Follow follow = Follow.builder()
                        .member(followParam.get(FOLLOWER))
                        .followMember(followParam.get(FOLLOWING))
                        .build();

        if (findFollow.isPresent()) {
            throw new DuplicateFollowException("이미 팔로우 했습니다.");
        }
        followRepository.save(follow);
    }

    @Transactional
    public void cancelFollowing(String userNickname, HttpServletRequest request) {
        List<Member> followParam = getFollowerAndFollowing(userNickname, request);
        Optional<Follow> findFollow = followRepository
                .findByMemberEqualsAndFollowMemberEquals(
                        followParam.get(FOLLOWER), followParam.get(FOLLOWING));

        findFollow.ifPresent(followRepository::delete);
    }

    public Followers getFollowers(String userNickname) {
        Member findMember = memberRepository.findByNickname(userNickname)
                .orElseThrow();

        List<String> nicknames = followRepository.findAllByFollowMember(findMember)
                .stream()
                .map(value -> value.getMember().getNickname())
                .collect(Collectors.toList());

        log.info("size={}",nicknames.size());
        log.info("nicknameList={}",nicknames.get(0));
        return new Followers(nicknames.size(), nicknames);
    }

    private List<Member> getFollowerAndFollowing(String userNickname, HttpServletRequest request) {
        Member loginMember = sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow();

        Member followMember = memberRepository.findByNickname(userNickname)
                .orElseThrow(()-> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        return List.of(findMember, followMember);
    }
}
