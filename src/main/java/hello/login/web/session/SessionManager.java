package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 스프링에 있는 세션을 사용하는 것이 아닌 세션 직접 생성해보기
 * 세션 관리
 */
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";


    //동시성 이슈(동시에 여러 쓰레드가 접근할 수 있는 경우)에는 ConcurrentHashMap을 사용.
    private Map<String,Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * 세션 저장소에 sessionId와 보관할 값 저장
     * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        //세션ID 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId,value);

        //쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie == null) {
            return null;
        }

        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if(sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
//                return sessionStore.get(cookie.getValue());
//            }
//        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);

//        findAny()는 찾는값중에 하나 [순서에 상관없다],  findFirst()는 제일 먼저 찾는값 [순서 상관있음.]
    }



}
