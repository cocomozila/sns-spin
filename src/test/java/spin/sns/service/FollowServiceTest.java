package spin.sns.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spin.sns.domain.follow.Follow;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.DuplicateFollowException;
import spin.sns.repository.FollowRepository;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.*;

class FollowServiceTest {

    @InjectMocks
    private FollowService followService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private FollowRepository followRepository;

    @BeforeEach
    public void testInit() {
        MockitoAnnotations.openMocks(this);
        followService = new FollowService(sessionRepository, memberRepository, followRepository);
    }

    @Test
    @DisplayName("정상적인 유저A의 유저B 팔로우")
    public void normalFollowTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Member member1 = Member.builder()
                .nickname("aa")
                .password("1234")
                .email("aa@naver.com")
                .introduceContext("hi everyone")
                .build();

        Member member2 = Member.builder()
                .nickname("bb")
                .password("1234")
                .email("bb@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(memberRepository.findByNickname(member2.getNickname())).thenReturn(Optional.of(member2));
        when(followRepository.findByMemberEqualsAndFollowMemberEquals(member1, member2)).thenReturn(Optional.empty());

        followService.following(member2.getNickname(), request);

        verify(followRepository, times(1)).save(any(Follow.class));
    }

    @Test
    @DisplayName("비정상적인 유저A의 유저B 팔로우 - 이미 중복된 팔로우")
    public void duplicatedFollowTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Member member1 = Member.builder()
                .nickname("aa")
                .password("1234")
                .email("aa@naver.com")
                .introduceContext("hi everyone")
                .build();

        Member member2 = Member.builder()
                .nickname("bb")
                .password("1234")
                .email("bb@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(memberRepository.findByNickname(member2.getNickname())).thenReturn(Optional.of(member2));
        when(followRepository.findByMemberEqualsAndFollowMemberEquals(member1, member2)).thenReturn(Optional.of(new Follow()));

        DuplicateFollowException exception = Assertions.assertThrows(DuplicateFollowException.class,
                () -> followService.following(member2.getNickname(), request));

        Assertions.assertEquals("이미 팔로우 했습니다.", exception.getMessage());
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    @DisplayName("정상적인 팔로우 취소 테스트")
    public void normalFollowCancelTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Member member1 = Member.builder()
                .nickname("aa")
                .password("1234")
                .email("aa@naver.com")
                .introduceContext("hi everyone")
                .build();

        Member member2 = Member.builder()
                .nickname("bb")
                .password("1234")
                .email("bb@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(memberRepository.findByNickname(member2.getNickname())).thenReturn(Optional.of(member2));
        when(followRepository.findByMemberEqualsAndFollowMemberEquals(member1, member2)).thenReturn(Optional.of(new Follow()));

        followService.cancelFollowing(member2.getNickname(), request);

        verify(followRepository, times(1)).delete(any(Follow.class));
    }
}