package spin.sns.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import spin.sns.domain.member.Member;
import spin.sns.domain.post.Post;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.PostRepository;
import spin.sns.repository.SessionRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private SessionRepository sessionRepository;

    @BeforeEach
    public void testInit() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, imageService, memberRepository, sessionRepository);
    }

    @Test
    @DisplayName("게시물 업로드 테스트")
    public void uploadPostTest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        postService.uploadPost(null, "새글 작성입니다!", request);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    public void deletePostTest() {
        postService.deletePost(anyLong());
        verify(postRepository, times(1)).deleteById(anyLong());
    }

}