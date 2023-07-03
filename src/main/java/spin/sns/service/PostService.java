package spin.sns.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spin.sns.domain.member.Member;
import spin.sns.domain.post.Post;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.PostRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final ImageService imageService;

    @Autowired
    private final SessionRepository sessionRepository;

    @Transactional
    public void uploadPost(List<MultipartFile> files, String content,
                           HttpServletRequest request) {

        Post post = postRepository.uploadPost(Post.builder()
                    .member((Member) sessionRepository.getSession(request))
                    .content(content)
                    .build());

        imageService.saveImage(files, post);
    }
}