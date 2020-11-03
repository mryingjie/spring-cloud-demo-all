package com.demo.springcloud.order.config;

import com.demo.springcloud.order.filter.HttpServletDesensitizationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Yingjie Zheng at 2020/4/23 09:48
 */
@Configuration
public class FilterConfig {

    @Value("${desensitization.filed}")
    private String field;


    // @Bean
    // public FilterRegistrationBean casSessionInitFilter(){
    //     FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    //     filterRegistrationBean.setFilter(new HttpServletDesensitizationFilter());
    //     filterRegistrationBean.addInitParameter("field", field);
    //     filterRegistrationBean.addUrlPatterns("/*");
    //     return filterRegistrationBean;
    // }


}
