package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    /**
     * 쿠키만을 이용한 로그인처리 [보안 취약]
     */
//    @PostMapping("/login")
    public String lgoin(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}",loginMember);

        if(loginMember == null ){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공처리

//        //쿠키에 시간 정보를 주지 않으면 세션 쿠키( 브라우저 종료시 모두 종료 )
//        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
//        response.addCookie(idCookie);
//        쿠키로 로그인할수 있지만, 쿠키 값은 위변조가 가능하여 보안에 굉장히 취약하다.
        return "redirect:/";
    }

    /**
     * 직접 만든 세션을 이용한 로그인처리
     */
//    @PostMapping("/login")
    public String lgoinV2(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}",loginMember);

        if(loginMember == null ){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공처리
        //세션 관리자를 통해 세션을 생성하고, 회원 데이터를 보관
        sessionManager.createSession(loginMember,response);

        return "redirect:/";
    }


    /**
     * 서블릿의 기본 세션을 이용한 로그인처리
     */
    @PostMapping("/login")
    public String lgoinV3(@Validated @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request ) {
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}",loginMember);

        if(loginMember == null ){
            bindingResult.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공처리
        //세션이 있으면 있는 세션을 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션 생성과 조회
        //세션을 생성하려면 request.getSession(true) 를 사용하면 된다. public HttpSession getSession(boolean create);
        //세션의 create 옵션에 대해 알아보자. request.getSession(true)
        //세션이 있으면 기존 세션을 반환한다.
        //세션이 없으면 새로운 세션을 생성해서 반환한다. request.getSession(false)
        //세션이 있으면 기존 세션을 반환한다.
        //세션이 없으면 새로운 세션을 생성하지 않는다. null 을 반환한다.


        //각각의 세션에 타임아웃을 설정해주려면 아래와 같이 하면 된다.
        session.setMaxInactiveInterval(1800);


        // 세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER,loginMember);



        return "redirect:/";
    }

    /**
     * 쿠키만을 이용한 로그인시 로그아웃 [사실상 쿠키 만료.]
     * */
//    @PostMapping("/logout")
    public String logout(HttpServletResponse response){
        expireCookie(response,"memberId");
        return "redirect:/";
    }

    /**
     * 직접 만든 세션을 이용한 로그아웃
     * */
//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request){
        sessionManager.expire(request);
        return "redirect:/";
    }

    /**
     * 서블릿의 기본 세션을 이용한 로그아웃 처리
     */
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
            //세션이랑 그안에 데이터까지 삭제
        }
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response,String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
