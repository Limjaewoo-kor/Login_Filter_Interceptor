package hello.login;

import hello.login.web.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WebConfig {

    //이렇게 해놓으면 스프링부트가 와스를 띄울때 같이 넣어준다.
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); //필터가 체인으로 여러개 들어갈시에 순서 설정.
        filterRegistrationBean.addUrlPatterns("/*"); //어떤 URL에 설정할것인가?

        return filterRegistrationBean;
    }
}
