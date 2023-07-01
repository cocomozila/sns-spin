package spin.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
}
