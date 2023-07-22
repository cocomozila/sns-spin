package spin.sns.repository;

import org.springframework.stereotype.Repository;
import spin.sns.domain.member.Member;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionRepository {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    private final Map<String, Member> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     */
    public void createSession(Member member, HttpServletResponse response) {

        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, member);

        //쿠키 생성
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        cookie.setPath("/api");
        response.addCookie(cookie);
    }

    /**
     * 세션 조회
     */
    public Member getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }
        return sessionStore.get(sessionCookie.getValue());
    }

    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
