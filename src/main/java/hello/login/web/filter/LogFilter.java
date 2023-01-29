package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");

        //ServletRequest는 HttpServletRequest의 부모이나, 기능이 적다, 그렇기에 HttpServletRequest으로 캐스트해준다.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 모든 사용자의 URI
        String requestURI = httpRequest.getRequestURI();

        //요청을 구분하기 위한 구분값 - uuid
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]",uuid,requestURI);
            //다음 필터가 있으면 다음필터가 호출 없으면 서블릿이 호출된다.
            chain.doFilter(request,response);
        }catch (Exception e){
            throw e;
        }finally {
            log.info("RESPONSE [{}][{}]",uuid,requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
