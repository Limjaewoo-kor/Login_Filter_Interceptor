package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import lombok.extern.java.Log;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    /**
     * 스프링 인터셉터 구간
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)  //첫번째 순서
                .addPathPatterns("/**")  //모든 URL
                .excludePathPatterns("/css/**", "/*.ico", "/error");  //이건 제외

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/member/add","/login"
                        ,"/logout","/css/**","/*.ico","/error");
    }

    /**
     * 서블릿 필터 구간
     */
    //이렇게 해놓으면 스프링부트가 와스를 띄울때 같이 넣어준다.
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); //필터가 체인으로 여러개 들어갈시에 순서 설정.
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 URL에 설정할것인가?

        return filterRegistrationBean;
    }


//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); //필터가 체인으로 여러개 들어갈시에 순서 설정.
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 URL에 설정할것인가? - 내부에 화이트리스트가 존재한다.

        return filterRegistrationBean;
    }
}
