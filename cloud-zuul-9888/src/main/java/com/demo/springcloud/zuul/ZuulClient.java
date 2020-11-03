package com.demo.springcloud.zuul;

import com.demo.springcloud.zuul.filter.InnocentRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 * created by Yingjie Zheng at 2020/4/9 10:00
 */
@SpringBootApplication
@EnableZuulProxy
public class ZuulClient {


    public static void main(String[] args) {
        SpringApplication.run(ZuulClient.class, args);
    }

    @Bean
    public InnocentRequestFilter innocentRequestFilter() {
        return new InnocentRequestFilter();
    }

    @Bean
    public FilterRegistrationBean casInnocentRequestFilter(InnocentRequestFilter innocentRequestFilter) {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(innocentRequestFilter);
        filterRegistrationBean.setOrder(13);
        filterRegistrationBean.addUrlPatterns("/*");

        // 避免与FilterConfig中的InnocentRequestFilter重名，导致后者失效
        filterRegistrationBean.setName("casInnocentRequestFilter");
        return filterRegistrationBean;
    }

}
