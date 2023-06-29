package spin.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spin.sns.annotation.CheckLogin;
import spin.sns.domain.member.*;
import spin.sns.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignup memberSignup) {
        memberService.signup(memberSignup.createEntity());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/find-account")
    public ResponseEntity<String> findAccount(@RequestBody EmailParam email) {
        return new ResponseEntity<>(memberService.findAccount(email.getEmail()), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginParam loginParam, HttpServletResponse response) {
        memberService.getLoginMember(loginParam, response);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/logout")
    @CheckLogin
    public void logout(HttpServletRequest request) {
        memberService.logout(request);
    }

    @PostMapping("find-password")
    public String findPassword(@RequestBody FindPasswordParam findPasswordParam) {
        return memberService.findPassword(findPasswordParam);
    }

    @PostMapping("/edit-password")
    @CheckLogin
    public ResponseEntity<Void> editPassword(@RequestBody EditPasswordParam editPasswordParam, HttpServletRequest request) {
        memberService.editPassword(editPasswordParam, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
