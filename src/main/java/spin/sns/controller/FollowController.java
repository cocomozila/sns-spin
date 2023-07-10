package spin.sns.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spin.sns.annotation.CheckLogin;
import spin.sns.domain.follow.Followers;
import spin.sns.service.FollowService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/follows")
public class FollowController {

    @Autowired
    private final FollowService followService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{userNickname}")
    @CheckLogin
    public void following(@PathVariable String userNickname, HttpServletRequest request) {
        followService.following(userNickname, request);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{userNickname}")
    @CheckLogin
    public void cancelFollowing(@PathVariable String userNickname, HttpServletRequest request) {
        followService.cancelFollowing(userNickname, request);
    }

    @PostMapping(value = "/{userNickname}/followers")
    public ResponseEntity<Followers> getFollowers(@PathVariable String userNickname) {
        Followers followers = followService.getFollowers(userNickname);
        return ResponseEntity.ok(followers);
    }
}
