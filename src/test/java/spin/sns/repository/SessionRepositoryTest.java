package spin.sns.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import spin.sns.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class SessionRepositoryTest {

    @Autowired SessionRepository sessionRepository;
    @Autowired MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        //회원 저장
        Member member = Member.builder()
                .nickname("seyun94")
                .password("123asd")
                .email("joseyun94@naver.com")
                .introduceContext("안녕하세요!")
                .build();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("세션 생성 테스트")
    void createSessionTest() {
        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member findMember = memberRepository.findByNickname("seyun94").get();
        sessionRepository.createSession(findMember, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        //세션 조회
        Object result = sessionRepository.getSession(request);
        assertThat(result).isEqualTo(findMember);
    }

    @Test
    @DisplayName("세션 만료 테스트")
    void sessionExpireTest() {
        //세션 생성
        MockHttpServletResponse response = new MockHttpServletResponse();
        Member findMember = memberRepository.findByNickname("seyun94").get();
        sessionRepository.createSession(findMember, response);

        //요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        sessionRepository.expire(request);
        Object expiredSession = sessionRepository.getSession(request);
        assertThat(expiredSession).isNull();
    }
}