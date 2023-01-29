package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * 쿠키만을 이용한 로그인처리 [세션X]
     */
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if(memberId == null ) {
            return "home";
        }

        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member",loginMember);
        return "loginHome";
    }

    /**
     * 직접 만든 세션을 이용한 로그인처리
     */
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request);

        if(member == null) {
            return "home";
        }

        model.addAttribute("member",member);
        return "loginHome";

    }


    /**
     * 서블릿의 기본 세션을 이용한 로그인처리
     */
//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //로그인시도가 아닌 최초에 사이트에 접속한 시점에는 세션을 만들 필요가 없다, false
        HttpSession session = request.getSession(false);

        //세션이 없으면 home로 이동
        if(session == null){
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home로 이동
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }



    /**
     * 스프링의 세션을 이용한 로그인처리
     * @SessionAttribute
     * 스프링은 세션을 더 편리하게 사용할 수 있도록 @SessionAttribute 을 지원한다.
     * 이미 로그인 된 사용자를 찾을 때는 다음과 같이 사용하면 된다. 참고로 이 기능은 세션을 생성하지 않는다.
     * @SessionAttribute(name = "loginMember", required = false) Member loginMember
     */
    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        //세션에 회원 데이터가 없으면 home로 이동
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member",loginMember);
        return "loginHome";
    }
}