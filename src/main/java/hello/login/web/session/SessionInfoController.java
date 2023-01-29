package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(session == null) {
            return "세션이 없습니다.";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",name,session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        //sessionId : 세션Id, JSESSIONID 의 값이다. 예) 34B14F008AA3527C9F8ED620EFD7A4E1 maxInactiveInterval : 세션의 유효 시간, 예) 1800초, (30분)
        //creationTime : 세션 생성일시
        //lastAccessedTime :세션과연결된사용자가최근에서버에접근한시간,클라이언트에서서버로
        //sessionId ( JSESSIONID )를 요청한 경우에 갱신된다.
        //isNew : 새로 생성된 세션인지, 아니면 이미 과거에 만들어졌고, 클라이언트에서 서버로
        //sessionId ( JSESSIONID )를 요청해서 조회된 세션인지 여부

        return "세션 출력";
    }
}
