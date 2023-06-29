package spin.sns.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spin.sns.domain.member.EditPasswordParam;
import spin.sns.domain.member.FindPasswordParam;
import spin.sns.domain.member.LoginParam;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.*;
import spin.sns.repository.MemberRepository;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        memberService = new MemberService(memberRepository, sessionRepository);
    }

    @Test
    @DisplayName("중복되지 않은 닉네임과 이메일로 회원 가입")
    public void testSignupWithUniqueNicknameAndEmail() {
        Member member = Member.builder()
                        .nickname("seyun")
                        .password("asdf")
                        .email("seyun94@naver.com")
                        .introduceContext("hi everyone")
                        .build();

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> memberService.signup(member));
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    @DisplayName("중복된 닉네임으로 회원 가입")
    public void testSignupWithDuplicateNickname() {
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.of(member));

        DuplicateNicknameException exception = assertThrows(DuplicateNicknameException.class, () -> memberService.signup(member));
        assertEquals("사용중인 닉네임입니다.", exception.getMessage());
        verify(memberRepository, never()).save(member);
    }

    @Test
    @DisplayName("중복된 이메일로 회원 가입")
    public void testSignupWithDuplicateEmail() {
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(memberRepository.findByNickname(member.getNickname())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> memberService.signup(member));
        assertEquals("사용중인 이메일입니다.", exception.getMessage());
        verify(memberRepository, never()).save(member);
    }

    @Test
    @DisplayName("이메일로 사용자 계정 찾기")
    public void testFindAccountWithEmailExists() {
        String email = "seyun94@naver.com";
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email(email)
                .introduceContext("hi everyone")
                .build();
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        String nickname = memberService.findAccount(email);
        assertEquals("seyun", nickname);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 계정 찾기")
    public void testFindAccountWithEmailDoesNotExist() {
        String email = "seyun94@naver.com";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        MemberNotExistException exception = assertThrows(MemberNotExistException.class, () -> memberService.findAccount(email));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유효한 자격 정보로 로그인한 사용자 가져오기")
    public void testGetLoginMemberWithValidCredentials() {
        LoginParam loginParam = new LoginParam("seyun", "asdf");
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        HttpServletResponse response = mock(HttpServletResponse.class);

        when(memberRepository.findByNickname(loginParam.getNickname())).thenReturn(Optional.of(member));
        doNothing().when(sessionRepository).createSession(member, response);

        Member result = memberService.getLoginMember(loginParam, response);
        assertEquals(member, result);

        verify(sessionRepository, times(1)).createSession(member, response);
    }

    @Test
    @DisplayName("유효하지 않은 자격 정보로 로그인한 경우")
    public void testGetLoginMemberWithInvalidCredentials() {
        LoginParam loginParam = new LoginParam("seyun", "wrong_password");
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(memberRepository.findByNickname(loginParam.getNickname())).thenReturn(Optional.of(member));

        PasswordMismatchException exception = assertThrows(PasswordMismatchException.class, () -> memberService.getLoginMember(loginParam, response));
        assertEquals("패스워드가 일치하지 않습니다.", exception.getMessage());

        verify(sessionRepository, never()).createSession(any(), any());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인한 경우")
    public void testGetLoginMemberWithNonexistentMember() {
        LoginParam loginParam = new LoginParam("seyun", "asdf");
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(memberRepository.findByNickname(loginParam.getNickname())).thenReturn(Optional.empty());

        MemberNotExistException exception = assertThrows(MemberNotExistException.class, () -> memberService.getLoginMember(loginParam, response));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());

        verify(sessionRepository, never()).createSession(any(), any());
    }

    @Test
    @DisplayName("로그아웃")
    public void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        memberService.logout(request);

        verify(sessionRepository, times(1)).expire(request);
    }

    @Test
    @DisplayName("유효한 아이디 이메일로 패스워드 찾기")
    public void findPasswordTest() {
        FindPasswordParam passwordParam = new FindPasswordParam("seyun","seyun94@naver.com");
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(memberRepository.findByNickname(passwordParam.getNickname())).thenReturn(Optional.of(member));
        String password = memberService.findPassword(passwordParam);
        assertEquals("asdf", password);
    }

    @Test
    @DisplayName("유효하지않는 아이디로 패스워드 찾기")
    public void findPasswordWithNicknameDoesNotExistTest() {
        FindPasswordParam passwordParam = new FindPasswordParam("wrong","seyun94@naver.com");
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(memberRepository.findByNickname(passwordParam.getNickname())).thenReturn(Optional.empty());
        MemberNotExistException exception = assertThrows(MemberNotExistException.class, () -> memberService.findPassword(passwordParam));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("유효하지않는 이메일로 패스워드 찾기")
    public void findPasswordWithEmailDoesNotExistTest() {
        FindPasswordParam passwordParam = new FindPasswordParam("seyun","wrong@naver.com");
        Member member = Member.builder()
                .nickname("seyun")
                .password("asdf")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(memberRepository.findByNickname(passwordParam.getNickname())).thenReturn(Optional.of(member));
        MemberNotExistException exception = assertThrows(MemberNotExistException.class, () -> memberService.findPassword(passwordParam));
        assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("정상 비밀번호 변경")
    public void editPasswordTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        EditPasswordParam editPasswordParam = new EditPasswordParam("normal", "editpassword", "editpassword");
        Member member = Member.builder()
                .nickname("seyun")
                .password("normal")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member);
        doNothing().when(memberRepository).editPassword(editPasswordParam, member.getNickname());

        memberService.editPassword(editPasswordParam, request);
        verify(memberRepository, times(1)).editPassword(editPasswordParam, member.getNickname());
    }

    @Test
    @DisplayName("비정상 비밀번호 변경_기존 비밀번호가 유효하지 않음")
    public void editPasswordTest_InvalidOldPassword() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        EditPasswordParam editPasswordParam = new EditPasswordParam("error_normal", "editpassword", "editpassword");
        Member member = Member.builder()
                .nickname("seyun")
                .password("normal")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member);

        PasswordMismatchException exception = assertThrows(PasswordMismatchException.class,
                () -> memberService.editPassword(editPasswordParam, request));
        assertEquals("패스워드가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("비정상 비밀번호 변경_변경할 비밀번호가 유효하지 않음")
    public void editPasswordTest_InvalidNewPassword() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        EditPasswordParam editPasswordParam = new EditPasswordParam("normal", "editpassword", "error_editpassword");
        Member member = Member.builder()
                .nickname("seyun")
                .password("normal")
                .email("seyun94@naver.com")
                .introduceContext("hi everyone")
                .build();

        when(sessionRepository.getSession(request)).thenReturn(member);

        PasswordConfirmationMismatchException exception = assertThrows(PasswordConfirmationMismatchException.class,
                () -> memberService.editPassword(editPasswordParam, request));
        assertEquals("변경할 비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}