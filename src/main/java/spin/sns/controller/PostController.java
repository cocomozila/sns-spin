package spin.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spin.sns.annotation.CheckLogin;
import spin.sns.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping("/upload")
    @CheckLogin
    public ResponseEntity<Void> uploadPost(@RequestParam String content,
                                           @RequestParam List<MultipartFile> files,
                                           HttpServletRequest request) {

        postService.uploadPost(files, content, request);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    @CheckLogin
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
    }

    @PutMapping("/{postId}")
    @CheckLogin
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@PathVariable Long postId,
                           @RequestParam String content,
                           HttpServletRequest request) {

        postService.updatePost(postId, content, request);
    }

    @PostMapping("/{postId}/like")
    @CheckLogin
    @ResponseStatus(HttpStatus.OK)
    public void addPostLike(@PathVariable Long postId, HttpServletRequest request) {
        postService.addPostLike(postId, request);
    }

    @DeleteMapping("/{postId}/like")
    @CheckLogin
    @ResponseStatus(HttpStatus.OK)
    public void deletePostLike(@PathVariable Long postId, HttpServletRequest request) {
        postService.deletePostLike(postId, request);
    }
}
