package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = {"/", "/member/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            log.info("2번 필터 시작 - 인증 필터 시작{}",requestURI);

            if(isLoginCheckPath(requestURI)) {

                log.info("인증 필터 실행{}",requestURI);
                HttpSession session = httpRequest.getSession(false);

                if(session==null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {

                    log.info("인증 되지않은 사용자 요청 {}",requestURI);
                    //로그인 페이지로 redirect 뒤에 requestURI 포함시켜주는 이유는 로그인시에 다시 해당 페이지로 돌아온다.
                    httpResponse.sendRedirect("/login?redirectURL="+requestURI);
                    return;
                }
            }
            chain.doFilter(request,response);

        } catch (Exception e) {
            throw e; //예외 로깅이 가능하지만, 톰캣까지 예외를 보내주어야한다.
        } finally {
            log.info("2번 필터 종료 - 인증 필터 종료 {}",requestURI);
        }

    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI){
        // 단순하게 패턴이 매칭되는가 체크한다. [매칭되지않으면 false]
        return !PatternMatchUtils.simpleMatch(whiteList,requestURI);
    }
}
