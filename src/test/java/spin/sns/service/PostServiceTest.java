package spin.sns.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import spin.sns.domain.member.Member;
import spin.sns.domain.post.Post;
import spin.sns.domain.postlike.PostLike;
import spin.sns.error.exception.PostPermissionDeniedException;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.PostLikeRepository;
import spin.sns.repository.PostRepository;
import spin.sns.repository.SessionRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Mock
    private PostLikeRepository postLikeRepository;

    private Member member;
    private MockHttpServletRequest request = new MockHttpServletRequest();

    @BeforeEach
    public void testInit() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, imageService, memberRepository, sessionRepository, postLikeRepository);
        member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();
    }

    @Test
    @DisplayName("게시물 업로드 테스트")
    public void uploadPostTest() {

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

    @Test
    @DisplayName("게시물 수정 테스트")
    public void updatePostTest() {

        Post post = mock(Post.class);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(sessionRepository.getSession(request)).thenReturn(member);
        when(post.isSameAuthors(member)).thenReturn(true);

        postService.updatePost(1L, "update", request);
        verify(post, times(1)).updateContent(anyString());
    }

    @Test
    @DisplayName("게시물 수정 에러 테스트 - 수정 권한 없음")
    public void updatePostErrorTest() {

        Post post = mock(Post.class);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(sessionRepository.getSession(request)).thenReturn(member);
        when(post.isSameAuthors(member)).thenReturn(false);

        PostPermissionDeniedException exception = assertThrows(PostPermissionDeniedException.class,
                () -> postService.updatePost(1L, "update", request));
        assertEquals("Post 수정 권한이 없습니다.", exception.getMessage());
        verify(post, never()).updateContent(anyString());
    }

    @Test
    @DisplayName("좋아요 등록 테스트")
    public void addPostLikeTest() {

        Post post = Post.builder()
                .member(member)
                .content("test")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postLikeRepository.findByMemberAndPost(member, post)).thenReturn(Optional.empty());
        postService.addPostLike(anyLong(), request);

        verify(postLikeRepository, times(1)).save(any(PostLike.class));
        Assertions.assertThat(post.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("좋아요 삭제 테스트")
    public void deletePostLikeTest() {

        Post post = Post.builder()
                        .member(member)
                        .content("test")
                        .build();

        PostLike postLike = PostLike.builder()
                        .post(post)
                        .member(member)
                        .build();

        when(sessionRepository.getSession(request)).thenReturn(member);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postLikeRepository.findByMemberAndPost(member, post)).thenReturn(Optional.of(postLike));

        postService.deletePostLike(anyLong(), request);
        verify(postLikeRepository,  times(1)).delete(postLike);
        Assertions.assertThat(post.getLikeCount()).isEqualTo(-1);
    }
}