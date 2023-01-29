# study-thymeleaf-basic_part3



Login 요약
- 세션 설정없이, 쿠키만으로 처리하는 로그인 및 로그아웃 [ 보안 취약 - 쿠키는 얼마든지 클라이언트측에서 임의로 수정 가능 ]
  -> 필요한 조건 2가지
      - 랜덤으로 변경하는 쿠키 추가 [UUID]
      - 일정시간이상 동작하지않으면 세션 invalidate 처리
- 서블릿에 있는 세션을 사용하지않고 세션 객체를 직접 개발하여 적용하는 로그인 및 로그아웃 [ 보안을 위한 random한 UUID 추가 ]
- 서블릿에서 지원하는 세션을 이용한 로그인 및 로그아웃 처리 + 타임아웃 처리 [ 일정시간 동작하지않으면 자동 로그아웃 처리 // 세션 invalidate]

Filter 요약
- doFilter의 ServletRequest는 HttpServletRequest의 부모이나, 기능이 적기에 HttpServletRequest으로 캐스트해서 사용하는게 일반적이다.
- 체인기능[ chain.doFilter(request,response); ]의 경우 -> 다음 필터가 있으면 다음 필터를 호출 없으면 서블릿이 호출된다.
            

Interceptor
