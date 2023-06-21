package spin.sns.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spin.sns.domain.member.EmailParam;
import spin.sns.domain.member.Member;
import spin.sns.domain.member.MemberSignup;
import spin.sns.domain.member.QMember;
import spin.sns.service.MemberService;

import javax.validation.Valid;
import java.util.Random;

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
}
