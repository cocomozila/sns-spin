package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spin.sns.domain.member.Member;
import spin.sns.domain.post.Post;
import spin.sns.domain.postlike.PostLike;
import spin.sns.error.exception.*;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.PostLikeRepository;
import spin.sns.repository.PostRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final SessionRepository sessionRepository;

    @Autowired
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void uploadPost(List<MultipartFile> files, String content,
                           HttpServletRequest request) {
        Member member = sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(member.getMemberId())
                .orElseThrow();

        Post post = postRepository.save(Post.builder()
                    .member(findMember)
                    .content(content)
                    .build());

        imageService.saveImage(files, post);
    }

    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public void updatePost(Long postId, String content, HttpServletRequest request) {
        Post findPost = postRepository.findById(postId).orElseThrow();
        Member loginMember = sessionRepository.getSession(request);
        if (findPost.isSameAuthors(loginMember)) {
            findPost.updateContent(content);
            return;
        }
        throw new PostPermissionDeniedException("Post 수정 권한이 없습니다.");
    }

    @Transactional
    public void addPostLike(Long postId, HttpServletRequest request) {
        Member loginMember = sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(()-> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("게시글을 찾을 수 없습니다."));
        if (postLikeRepository.findByMemberAndPost(findMember, findPost).isPresent()) {
            throw new DuplicatePostLikeException("좋아요한 게시물입니다.");
        }
        postLikeRepository.save(new PostLike(findPost, findMember));
        findPost.addLike();
    }

    @Transactional
    public void deletePostLike(Long postId, HttpServletRequest request) {
        Member loginMember = sessionRepository.getSession(request);
        Member findMember = memberRepository.findById(loginMember.getMemberId())
                .orElseThrow(()-> new MemberNotExistException("사용자를 찾을 수 없습니다."));

        Post findPost = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        Optional<PostLike> findPostLIke = postLikeRepository.findByMemberAndPost(findMember, findPost);
        if (findPostLIke.isPresent()) {
            postLikeRepository.delete(findPostLIke.get());
            findPost.deleteLike();
            return;
        }
        throw new PostLikeNotFoundException("좋아요가 존재하지 않습니다.");
    }
}
