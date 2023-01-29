package hello.login.web.session;


import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    /**
     * 테스트에서 HttpServletResponse, HttpServletRequest를 이용할 수 없기에 Mock를 사용한다.
     */
    @Test
    void sessionTest() {

        MockHttpServletResponse response = new MockHttpServletResponse();

        // 세션 생성
        Member member = new Member();
        sessionManager.createSession(member,response);

        // 요청에 응답 쿠키 저장
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());

        // 서버에 요청하여 조회
        Object result = sessionManager.getSession(request);
        Assertions.assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(request);

        Object expire = sessionManager.getSession(request);
        Assertions.assertThat(expire).isNull();

    }



}
