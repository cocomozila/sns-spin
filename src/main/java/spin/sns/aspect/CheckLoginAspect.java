package spin.sns.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import spin.sns.domain.member.Member;
import spin.sns.error.exception.NotLoginException;
import spin.sns.repository.SessionRepository;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckLoginAspect {

    private final SessionRepository sessionRepository;

    @Before("@annotation(spin.sns.annotation.CheckLogin)")
    public void checkLogin(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Member session = sessionRepository.getSession(request);
        if (session == null) {
            throw new NotLoginException("로그인이 필요합니다.");
        }
    }
}
